package us.stupidx.dailygoal;

import us.stupidx.config.DailyGoal_tbl;
import us.stupidx.db.GoalOpenHelper;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
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

	}

	// 添加按钮的监听
	private class AddGoalListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Cursor cursor = openHelper.readCurrentGoal();
			if (cursor.getCount() > 0) {
				Log.i("cursor.getCount()", "" + cursor.getCount());
				// update
				int updatedCount = openHelper.updateCurrentGoal(etGoalContent
						.getText().toString());
				Log.i("updatedCount", "" + updatedCount);
			} else {
				Long insertedId = openHelper.insertCurrentGoal(etGoalContent
						.getText().toString());
				Log.i("insertedId", "" + insertedId);
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
