package us.stupidx.dailygoal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.activity_start, null);
		setContentView(view);

		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
		aa.setDuration(1000);
		view.startAnimation(aa);

		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				redirectTo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
	}

	protected void redirectTo() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

}
