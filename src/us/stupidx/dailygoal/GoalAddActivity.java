package us.stupidx.dailygoal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class GoalAddActivity extends Activity {
	EditText goalContent;
	Button confirmBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goal_add);

		goalContent = (EditText) findViewById(R.id.goal_content);
		confirmBtn = (Button) findViewById(R.id.confirm_btn);

		confirmBtn.setOnClickListener(confirmLsn);
	}

	OnClickListener confirmLsn = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Log.d("OnClickListener ", goalContent.getText().toString());
			stopService(new Intent(GoalAddActivity.this, NotifyingService.class));
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.goal_add, menu);
		return true;
	}

}
