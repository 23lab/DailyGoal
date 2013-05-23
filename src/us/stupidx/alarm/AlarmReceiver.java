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
			// ��1�������õ�����ʱ�䵽��������Ե���������ʾ����������
			// ���Լ���������һ������ʱ��;
			// Toast.makeText(context, new Date().getTime() + "",
			// Toast.LENGTH_LONG).show();

			// ��ʱ���ܹ㲥, �������Ƿ���Ҫ֪ͨ������
			mNotificationManager = (NotificationManager) context
					.getSystemService(Activity.NOTIFICATION_SERVICE); // ��ʼ��������

			// ����һ��֪ͨʵ������һ��������ͼƬ���ڶ�������������ʾ�����֣���������ʱ��
			Notification notification = new Notification(R.drawable.ic_launcher, "Notification",
					System.currentTimeMillis());

			openHelper = new GoalOpenHelper(context);
			Cursor cursor = openHelper.readCurrentGoal();
			if (cursor.getCount() == 0 && isSetGoalTime(context)) {
				// ���컹û������Ŀ��
				Intent archiveIntent = new Intent(context, ArchiveActivity.class);
				intent.putExtra("from_ntf", true);
				// ���������������ı�������ʱ����ʲô����������ת�������档�����������һ��ġ�
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, archiveIntent,
						0);

				// Title ���������ı��⣬ContentҲ���������������ʾ
				notification.setLatestEventInfo(context, context.getString(R.string.no_goal_today),
						context.getString(R.string.ask_user_to_set_goal), contentIntent);

				// ��ʾ���֪ͨ
				mNotificationManager.notify(Config.NTF_SETGOAL_ID, notification);
			} else if (cursor.getCount() == 1 && isReviewTime(context) && isTodayFinish()) {
				// �Ѿ�������Ŀ��, ���ǻ�û�����
				Intent archiveIntent = new Intent(context, ArchiveActivity.class);
				intent.putExtra("from_ntf", true);
				// ���������������ı�������ʱ����ʲô����������ת�������档�����������һ��ġ�
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, archiveIntent,
						0);

				// Title ���������ı��⣬ContentҲ���������������ʾ
				notification.setLatestEventInfo(context,
						context.getString(R.string.is_goal_finish),
						context.getString(R.string.ask_user_review), contentIntent);

				// ��ʾ���֪ͨ
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