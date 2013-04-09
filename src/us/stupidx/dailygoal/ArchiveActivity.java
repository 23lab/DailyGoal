package us.stupidx.dailygoal;

import us.stupidx.config.Config;
import us.stupidx.config.DailyGoal_tbl;
import us.stupidx.db.GoalOpenHelper;
import android.app.Activity;
import android.app.NotificationManager;
import android.database.Cursor;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ArchiveActivity extends Activity {
	private EditText etGoalContent;
	private ListView goalList;
	private GoalOpenHelper openHelper;
	private Cursor goalListCursor;

	private void fillGoalList() {
		String[] dataColumns = { DailyGoal_tbl.GoalColumn.COL_DATE,
				DailyGoal_tbl.GoalColumn.COL_CTN };

		int[] viewIDs = { R.id.goal_list_item_date, R.id.goal_list_item_ctn };

		// 从数据库读取所有goals
		goalListCursor = openHelper.readAll();
		@SuppressWarnings("deprecation")
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.goal_list_item, // Points to the XML for a list item
				goalListCursor, // The cursor to get items from
				dataColumns, viewIDs);
		// Sets the ListView's adapter to be the cursor adapter that was just
		// created.
		goalList = (ListView) findViewById(R.id.goal_list);
		goalList.setAdapter(adapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive);
		openHelper = new GoalOpenHelper(this);
		etGoalContent = (EditText) findViewById(R.id.goal_content);

		this.fillGoalList();

		findViewById(R.id.add_goal_btn).setOnClickListener(
				new AddGoalListener());

		GestureOverlayView gov = (GestureOverlayView) findViewById(R.id.archive_gesture_ov);
		gov.setGestureVisible(true);
		gov.addOnGestureListener(new NavGestureListener(this, null,
				HomeActivity.class));

		Button rtnBtn = (Button) findViewById(R.id.archive_return_btn);

		rtnBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArchiveActivity.this.finish();
				ArchiveActivity.this.overridePendingTransition(
						R.anim.push_left_in, R.anim.push_left_out);
			}
		});

		NotificationManager mNotificationManager = (NotificationManager) this
				.getSystemService(Activity.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(Config.NTF_SETGOAL_ID);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}

	// 添加按钮的监听
	private class AddGoalListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Cursor cursor = openHelper.readCurrentGoal();
			if (cursor.getCount() > 0) {
				openHelper.updateCurrentGoal(etGoalContent
						.getText().toString());
			} else {
				openHelper.insertCurrentGoal(etGoalContent
						.getText().toString());
			}
			ArchiveActivity.this.fillGoalList();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		openHelper.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.archive, menu);
		return true;
	}

}
