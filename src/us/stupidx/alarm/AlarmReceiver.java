package us.stupidx.alarm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import us.stupidx.config.Config;
import us.stupidx.config.DailyGoal_tbl;
import us.stupidx.dailygoal.ArchiveActivity;
import us.stupidx.dailygoal.R;
import us.stupidx.db.GoalOpenHelper;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	private NotificationManager mNotificationManager;
	private GoalOpenHelper openHelper;

	public void onReceive(Context context, Intent intent) {
		if (Config.ALARM_ACTION.equals(intent.getAction())) {
			// 第1步中设置的闹铃时间到，这里可以弹出闹铃提示并播放响铃
			// 可以继续设置下一次闹铃时间;
			// Toast.makeText(context, new Date().getTime() + "",
			// Toast.LENGTH_LONG).show();

			// 定时接受广播, 这里检测是否需要通知栏提醒
			mNotificationManager = (NotificationManager) context
					.getSystemService(Activity.NOTIFICATION_SERVICE); // 初始化管理器

			// 建立一个通知实例，第一个参数是图片，第二个标题栏上显示的文字，第三个是时间
			Notification notification = new Notification(R.drawable.ic_launcher, "Notification",
					System.currentTimeMillis());

			openHelper = new GoalOpenHelper(context);
			Cursor cursor = openHelper.readCurrentGoal();
			if (cursor.getCount() == 0 && isSetGoalTime(context)) {
				// 今天还没有设置目标
				Intent archiveIntent = new Intent(context, ArchiveActivity.class);
				intent.putExtra("from_ntf", true);
				// 当单击下拉下来的标题内容时候做什么，这里是跳转到主界面。这里和下面是一起的。
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, archiveIntent,
						0);

				// Title 是拉下来的标题，Content也是下拉后的内容显示
				notification.setLatestEventInfo(context, context.getString(R.string.no_goal_today),
						context.getString(R.string.ask_user_to_set_goal), contentIntent);

				// 显示这个通知
				mNotificationManager.notify(Config.NTF_SETGOAL_ID, notification);
			} else if (cursor.getCount() == 1 && isReviewTime(context) && isTodayFinish()) {
				// 已经设置了目标, 但是还没有完成
				Intent archiveIntent = new Intent(context, ArchiveActivity.class);
				intent.putExtra("from_ntf", true);
				// 当单击下拉下来的标题内容时候做什么，这里是跳转到主界面。这里和下面是一起的。
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, archiveIntent,
						0);

				// Title 是拉下来的标题，Content也是下拉后的内容显示
				notification.setLatestEventInfo(context,
						context.getString(R.string.is_goal_finish),
						context.getString(R.string.ask_user_review), contentIntent);

				// 显示这个通知
				mNotificationManager.notify(Config.NTF_REVIEW_ID, notification);
			}
		}
	}

	private boolean isTodayFinish() {
		Cursor cursor = openHelper.readCurrentGoal();
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			String isFinish = cursor.getString(cursor
					.getColumnIndex(DailyGoal_tbl.GoalColumn.COL_FINISH_AT));
			return isFinish == null ? false : true;
		}

		return false;
	}

	private boolean isReviewTime(Context context) {
		SharedPreferences settings = context.getSharedPreferences(Config.PREFS_NAME, 0);

		String mTime = settings.getString(Config.AFTERNOON_TIME, Config.DEFAULT_AFTERNOON_TIME);
		SimpleDateFormat sdf = new SimpleDateFormat("H:m", Locale.CHINA);
		Date today = new Date();
		if (sdf.format(today).equals(mTime)) {
			Log.d("isReviewTime", sdf.format(today) + " ==? " + mTime + " true");
			return true;
		} else {
			Log.d("isReviewTime", sdf.format(today) + " ==? " + mTime + " false");
			return false;
		}
	}

	private boolean isSetGoalTime(Context context) {
		SharedPreferences settings = context.getSharedPreferences(Config.PREFS_NAME, 0);
		String aTime = settings.getString(Config.MORNING_TIME, Config.DEFAULT_MORNING_TIME);
		SimpleDateFormat sdf = new SimpleDateFormat("H:m", Locale.CHINA);
		Date today = new Date();
		Log.i("sdf.format(today): ", sdf.format(today));
		if (sdf.format(today).compareTo(aTime) > 0) {
			Log.i("isSetGoalTime", sdf.format(today) + " true");
			return true;
		} else {
			Log.d("isSetGoalTime", sdf.format(today) + " false");
			return false;
		}
	}
}