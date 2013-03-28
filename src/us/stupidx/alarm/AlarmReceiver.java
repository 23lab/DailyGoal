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
			// ��1�������õ�����ʱ�䵽��������Ե���������ʾ����������
			// ���Լ���������һ������ʱ��;
			Toast.makeText(context, new Date().getTime() + "",
					Toast.LENGTH_LONG).show();

			// ��ʱ���ܹ㲥, �������Ƿ���Ҫ֪ͨ������
			mNotificationManager = (NotificationManager) context
					.getSystemService(Activity.NOTIFICATION_SERVICE); // ��ʼ��������

			// ����һ��֪ͨʵ������һ��������ͼƬ���ڶ�������������ʾ�����֣���������ʱ��
			Notification notification = new Notification(
					R.drawable.ic_launcher, "Notification",
					System.currentTimeMillis());

			Intent archiveIntent = new Intent(context, ArchiveActivity.class);
			intent.putExtra("from_ntf", true);
			// ���������������ı�������ʱ����ʲô����������ת�������档�����������һ��ġ�
			PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
					archiveIntent, 0);

			// Title ���������ı��⣬ContentҲ���������������ʾ
			notification.setLatestEventInfo(context, "NTF Title",
					"NTF Content", contentIntent);

			// ��ʾ���֪ͨ
			mNotificationManager.notify(NOTIFICATIONS_ID, notification);
		}
	}

	private static int NOTIFICATIONS_ID = 1; // ��ǰҳ��Ĳ���
}