package us.stupidx.dailygoal;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.ConditionVariable;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class NotifyingService extends Service {
	private NotificationManager mNM;
	private ConditionVariable mCondition;
	private static int MOOD_NOTIFICATIONS = 9999;

	private final IBinder mBinder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};

	@Override
	public void onCreate() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Start up the thread running the service. Note that we create a
		// separate thread because the service normally runs in the process's
		// main thread, which we don't want to block.
		Thread notifyingThread = new Thread(null, mTask, "NotifyingService");
		mCondition = new ConditionVariable(false);
		notifyingThread.start();
	}

	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
		mNM.cancel(MOOD_NOTIFICATIONS);
		// Stop the thread from generating further notifications
		mCondition.open();
	}

	private Runnable mTask = new Runnable() {
		public void run() {
			while (true) {
				Calendar rightNow = Calendar.getInstance();
				if (rightNow.get(Calendar.HOUR_OF_DAY) >= 9
						&& rightNow.get(Calendar.HOUR_OF_DAY) <= 24) {
					showNotification(R.drawable.ic_launcher, R.string.app_name);
					Log.d("NotifyingService", "task run : " + rightNow.getTimeInMillis());
				}

				mCondition.block(5 * 1000);
			}
			// Done with our work... stop the service!
			// NotifyingService.this.stopSelf();
		}
	};

	void showToast() {
		// show the toast
		Toast toast = new Toast(this);
		toast.setText("Service Active");
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	@SuppressWarnings("deprecation")
	protected void showNotification(int moodId, int textId) {
		CharSequence text = getText(textId);

		Notification notification = new Notification(moodId, null, System.currentTimeMillis());

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				GoalAddActivity.class), 0);

		notification.setLatestEventInfo(this, getText(R.string.app_name), text, contentIntent);

		mNM.notify(MOOD_NOTIFICATIONS, notification);
	}

}
