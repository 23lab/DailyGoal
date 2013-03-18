package us.stupidx.dailygoal;

import us.stupidx.config.Config;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		//TimePicker tp = (TimePicker)findViewById(R.id.morning_time);
		
		Button saveBtn = (Button) findViewById(R.id.save_setting);
		saveBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				SettingsActivity.this.saveSettings();
			}
			
		});
		
	}
	
	public void saveSettings() {
		TimePicker morningTimePicker = (TimePicker)findViewById(R.id.morning_time);
		TimePicker afternoonTimePicker = (TimePicker)findViewById(R.id.afternoon_time);
		
		String morningTime = morningTimePicker.getCurrentHour() + ":" + morningTimePicker.getCurrentMinute();
		String afternoonTime = afternoonTimePicker.getCurrentHour() + ":" + afternoonTimePicker.getCurrentMinute();
		
		SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME,  MODE_WORLD_WRITEABLE);

		Editor editor= settings.edit();
		editor.putString("morning_time", morningTime);
		editor.putString("afternoon_time", afternoonTime);
		editor.commit();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
