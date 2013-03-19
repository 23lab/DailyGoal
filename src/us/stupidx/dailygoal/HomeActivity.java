package us.stupidx.dailygoal;

import us.stupidx.config.Config;
import us.stupidx.config.Direction;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.Toast;

public class HomeActivity extends Activity {
	Point gestureStart = new Point(), gestureEnd = new Point();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		SharedPreferences settings = this.getSharedPreferences(
				Config.PREFS_NAME, 0);

		Toast.makeText(this, settings.getString("morning_time", "moring_time"),
				Toast.LENGTH_LONG).show();

		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.home_guesture_view);
		gestures.setGestureVisible(false);
		gestures.addOnGestureListener(new OnGestureListener() {

			final int validDragDistance = 100;

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
				} else if (gestureStart.y - gestureEnd.x > Config.VALID_DRAG_DISTANCE){
					this.redirectTo(GoalAddActivity.class, Direction.UP);
				}
			}

			@Override
			public void onGestureStarted(GestureOverlayView arg0,
					MotionEvent arg1) {
				gestureStart.x = (int) arg1.getX();
				gestureStart.y = (int) arg1.getY();
			}

			private double distance(Point first, Point second) {
				int subX = Math.abs(second.x - first.x);
				int subY = Math.abs(second.y - first.y);
				return Math.sqrt(Math.pow(subX, 2) + Math.pow(subY, 2));
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

}
