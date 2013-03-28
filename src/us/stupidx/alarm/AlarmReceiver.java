package us.stupidx.alarm;

import java.util.Date;

import us.stupidx.dailygoal.ArchiveActivity;
import us.stupidx.dailygoal.R;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	private NotificationManager mNotificationManager;

	public void onReceive(Context context, Intent intent) {
		if ("android.alarm.demo.action".equals(intent.getAction())) {
			// 第1步中设置的闹铃时间到，这里可以弹出闹铃提示并播放响铃
			// 可以继续设置下一次闹铃时间;
			Toast.makeText(context, new Date().getTime() + "",
					Toast.LENGTH_LONG).show();

			// 定时接受广播, 这里检测是否需要通知栏提醒
			mNotificationManager = (NotificationManager) context
					.getSystemService(Activity.NOTIFICATION_SERVICE); // 初始化管理器

			// 建立一个通知实例，第一个参数是图片，第二个标题栏上显示的文字，第三个是时间
			Notification notification = new Notification(
					R.drawable.ic_launcher, "Notification",
					System.currentTimeMillis());

			Intent archiveIntent = new Intent(context, ArchiveActivity.class);
			intent.putExtra("from_ntf", true);
			// 当单击下拉下来的标题内容时候做什么，这里是跳转到主界面。这里和下面是一起的。
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					archiveIntent, 0);

			// Title 是拉下来的标题，Content也是下拉后的内容显示
			notification.setLatestEventInfo(context, "NTF Title",
					"NTF Content", contentIntent);

			// 显示这个通知
			mNotificationManager.notify(NOTIFICATIONS_ID, notification);
		}
	}

	private static int NOTIFICATIONS_ID = 1; // 当前页面的布局
}