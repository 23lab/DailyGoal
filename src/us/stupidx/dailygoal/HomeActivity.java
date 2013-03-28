package us.stupidx.dailygoal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import us.stupidx.config.Config;
import us.stupidx.config.DailyGoal_tbl;
import us.stupidx.db.GoalOpenHelper;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	GoalOpenHelper openHelper;
	private CheckBox cbTodayGoal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		SharedPreferences settings = this.getSharedPreferences(
				Config.PREFS_NAME, 0);

		// 如果app还未设置时间
		if (settings.getString(Config.MORNING_TIME, "").equals("")) {
			Editor editSetting = settings.edit();
			editSetting.putString(Config.MORNING_TIME,
					Config.DEFAULT_MORNING_TIME);
			editSetting.putString(Config.AFTERNOON_TIME,
					Config.DEFAULT_AFTERNOON_TIME);
			editSetting.commit();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
		Date today = new Date();
		((TextView) findViewById(R.id.today_date_tv))
				.setText(sdf.format(today));
		((TextView) findViewById(R.id.today_day_tv)).setText("星期"
				+ today.getDay());

		cbTodayGoal = (CheckBox) findViewById(R.id.today_goal_cb);
		cbTodayGoal.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					Toast.makeText(HomeActivity.this, "Yes", Toast.LENGTH_SHORT)
							.show();
					openHelper.finishiGoal();
				} else {
					Toast.makeText(HomeActivity.this, "No", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.home_guesture_view);
		gestures.setGestureVisible(false);
		gestures.addOnGestureListener(new NavGestureListener(this,
				ArchiveActivity.class, SettingsActivity.class));

		findViewById(R.id.setting_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						HomeActivity.this.startActivity(new Intent(
								HomeActivity.this, SettingsActivity.class));
						HomeActivity.this.overridePendingTransition(
								R.anim.push_left_in, R.anim.push_left_out);
					}
				});

		findViewById(R.id.archive_btn).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						HomeActivity.this.startActivity(new Intent(
								HomeActivity.this, ArchiveActivity.class));
						HomeActivity.this.overridePendingTransition(
								R.anim.push_right_in, R.anim.push_right_out);
					}
				});

		// 设置闹铃
		this.setAlarmTime(this, 1000);

	}

	private void setAlarmTime(Context context, long timeInMillis) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(Config.ALARM_ACTION);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		int interval = 60 * 1000;// 闹铃间隔， 这里设为1分钟闹一次，在第2步我们将每隔1分钟收到一次广播]
		am.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, interval, sender);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		openHelper.close();
	}

	@Override
	protected void onResume() {
		super.onResume();
		openHelper = new GoalOpenHelper(this);
		Cursor cursor = openHelper.readCurrentGoal();
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			String ctn = cursor.getString(cursor
					.getColumnIndex(DailyGoal_tbl.GoalColumn.COL_CTN));
			String finishiAt = cursor.getString(cursor
					.getColumnIndex(DailyGoal_tbl.GoalColumn.COL_FINISH_AT));
			if (finishiAt != null) {
				cbTodayGoal.setChecked(true);
				cbTodayGoal.setEnabled(false);
			}
			cbTodayGoal.setText(ctn);
		}
	}
}
