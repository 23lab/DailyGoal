package us.stupidx.dailygoal;

import us.stupidx.config.Config;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		this.fillCurrentSetting();
		// TimePicker tp = (TimePicker)findViewById(R.id.morning_time);

		Button saveBtn = (Button) findViewById(R.id.save_setting);
		saveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (SettingsActivity.this.saveSettings()) {
					Toast.makeText(SettingsActivity.this, "设置保存成功",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(SettingsActivity.this, "设置保存成功",
							Toast.LENGTH_LONG).show();
				}
			}

		});

	}

	private void fillCurrentSetting() {
		SharedPreferences settings = this.getSharedPreferences(
				Config.PREFS_NAME, 0);
		String morningTime = settings.getString(Config.MORNING_TIME, null);
		if (morningTime != null) {
			TimePicker tp = (TimePicker) findViewById(R.id.morning_time);

			String[] time = morningTime.split(":");
			tp.setCurrentHour(Integer.parseInt(time[0]));
			tp.setCurrentMinute(Integer.parseInt(time[1]));
		}

		String afternoonTime = settings.getString(Config.AFTERNOON_TIME, null);
		if (afternoonTime != null) {
			TimePicker tp = (TimePicker) findViewById(R.id.afternoon_time);
			String[] time = afternoonTime.split(":");
			tp.setCurrentHour(Integer.parseInt(time[0]));
			tp.setCurrentMinute(Integer.parseInt(time[0]));
		}
	}

	@SuppressLint("WorldWriteableFiles")
	@SuppressWarnings("deprecation")
	public boolean saveSettings() {
		TimePicker morningTimePicker = (TimePicker) findViewById(R.id.morning_time);
		TimePicker afternoonTimePicker = (TimePicker) findViewById(R.id.afternoon_time);

		String morningTime = morningTimePicker.getCurrentHour() + ":"
				+ morningTimePicker.getCurrentMinute();
		String afternoonTime = afternoonTimePicker.getCurrentHour() + ":"
				+ afternoonTimePicker.getCurrentMinute();

		SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME,
				MODE_WORLD_WRITEABLE);

		Editor editor = settings.edit();
		editor.putString(Config.MORNING_TIME, morningTime);
		editor.putString(Config.AFTERNOON_TIME, afternoonTime);
		return editor.commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
