package us.stupidx.dailygoal;

import us.stupidx.config.Config;
import us.stupidx.config.Direction;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGestureListener;
import android.graphics.Point;
import android.view.MotionEvent;

public class NavGestureListener implements OnGestureListener {
	private Point gestureStart, gestureEnd;
	private Class<? extends Activity> left;
	private Class<? extends Activity> right;
	private Activity act;

	public NavGestureListener(Activity ctx, Class<? extends Activity> left,
			Class<? extends Activity> right) {
		super();
		this.act = ctx;

		this.left = left;
		this.right = right;

		gestureStart = new Point();
		gestureEnd = new Point();
	}

	@Override
	public void onGesture(GestureOverlayView arg0, MotionEvent arg1) {
	}

	@Override
	public void onGestureCancelled(GestureOverlayView arg0, MotionEvent arg1) {
	}

	@Override
	public void onGestureEnded(GestureOverlayView arg0, MotionEvent arg1) {
		gestureEnd.x = (int) arg1.getX();
		gestureEnd.y = (int) arg1.getY();

		if (gestureEnd.x - gestureStart.x > Config.VALID_DRAG_DISTANCE) {
			if (left != null) {
				this.redirectTo(this.left, Direction.LEFT);
			}
		} else if (gestureStart.x - gestureEnd.x > Config.VALID_DRAG_DISTANCE) {
			if (right != null) {
				this.redirectTo(this.right, Direction.RIGHT);
			}
		}
	}

	@Override
	public void onGestureStarted(GestureOverlayView arg0, MotionEvent arg1) {
		gestureStart.x = (int) arg1.getX();
		gestureStart.y = (int) arg1.getY();
	}

	private void redirectTo(Class<? extends Activity> c, Direction d) {

		Intent intent = new Intent(this.act, c);
		if (c.equals(HomeActivity.class)) {
			this.act.finish();
		} else {
			this.act.startActivity(intent);
		}

		if (Direction.LEFT.equals(d)) {
			this.act.overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
		} else if (Direction.RIGHT.equals(d)) {
			this.act.overridePendingTransition(R.anim.push_left_in,
					R.anim.push_left_out);
		}
	}

}
