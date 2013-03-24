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
	Point gestureStart = new Point(), gestureEnd = new Point();
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
		((TextView) findViewById(R.id.today_date_tv)).setText(sdf.format(today));
		((TextView) findViewById(R.id.today_day_tv)).setText("星期" + today.getDay());
		
		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.home_guesture_view);
		gestures.setGestureVisible(false);
		gestures.addOnGestureListener(new OnGestureListener() {

			@Override
			public void onGesture(GestureOverlayView arg0, MotionEvent arg1) {
			}

			@Override
			public void onGestureCancelled(GestureOverlayView arg0,
					MotionEvent arg1) {
			}

			@Override
			public void onGestureEnded(GestureOverlayView arg0, MotionEvent arg1) {
				gestureEnd.x = (int) arg1.getX();
				gestureEnd.y = (int) arg1.getY();

				if (gestureEnd.x - gestureStart.x > Config.VALID_DRAG_DISTANCE) {
					this.redirectTo(ArchiveActivity.class, Direction.RIGHT);
				} else if (gestureStart.x - gestureEnd.x > Config.VALID_DRAG_DISTANCE) {
					this.redirectTo(SettingsActivity.class, Direction.LEFT);
				} else if (gestureStart.y - gestureEnd.x > Config.VALID_DRAG_DISTANCE) {
					// this.redirectTo(GoalAddActivity.class, Direction.UP);
				}
			}

			@Override
			public void onGestureStarted(GestureOverlayView arg0,
					MotionEvent arg1) {
				gestureStart.x = (int) arg1.getX();
				gestureStart.y = (int) arg1.getY();
			}

			private void redirectTo(Class<? extends Activity> c, Direction d) {

				Intent intent = new Intent(HomeActivity.this, c);
				startActivity(intent);

				if (Direction.UP.equals(d)) {

				} else if (Direction.DOWN.equals(d)) {

				} else if (Direction.LEFT.equals(d)) {
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);

				} else if (Direction.RIGHT.equals(d)) {
					overridePendingTransition(R.anim.push_right_in,
							R.anim.push_right_out);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		openHelper = new GoalOpenHelper(this);
		Cursor cursor = openHelper.readCurrentGoal();
		cursor.moveToFirst();
		String ctn = cursor.getString(cursor.getColumnIndex(DailyGoal_tbl.GoalColumn.COL_CTN));
		
		TextView tvTodayGoal = (TextView)findViewById(R.id.today_goal_tv);
		tvTodayGoal.setText(ctn);
	}

	public void x(){
		openHelper = new GoalOpenHelper(this);
		Cursor cursor = openHelper.readCurrentGoal();
		cursor.moveToFirst();
		String ctn = cursor.getString(cursor.getColumnIndex(DailyGoal_tbl.GoalColumn.COL_CTN));
		
		TextView tvTodayGoal = (TextView)findViewById(R.id.today_goal_tv);
		tvTodayGoal.setText(ctn);
	}
}
