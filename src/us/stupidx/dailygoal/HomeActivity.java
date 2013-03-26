package us.stupidx.dailygoal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import us.stupidx.config.Config;
import us.stupidx.config.DailyGoal_tbl;
import us.stupidx.config.Direction;
import us.stupidx.db.GoalOpenHelper;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	GoalOpenHelper openHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		SharedPreferences settings = this.getSharedPreferences(
				Config.PREFS_NAME, 0);

		Toast.makeText(this,
				settings.getString(Config.MORNING_TIME, "no morning time"),
				Toast.LENGTH_LONG).show();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
		Date today = new Date();
		((TextView) findViewById(R.id.today_date_tv))
				.setText(sdf.format(today));
		((TextView) findViewById(R.id.today_day_tv)).setText("星期"
				+ today.getDay());

		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.home_guesture_view);
		gestures.setGestureVisible(false);
		gestures.addOnGestureListener(new NavGestureListener(this,
				ArchiveActivity.class, SettingsActivity.class));
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
			TextView tvTodayGoal = (TextView) findViewById(R.id.today_goal_tv);
			tvTodayGoal.setText(ctn);
		}
	}
}
