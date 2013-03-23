package us.stupidx.dailygoal;

import us.stupidx.config.DailyGoal_tbl;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

public class ArchiveActivity extends ListActivity {

	private EditText etGoalContent;

	private static final String[] PROJECTION = new String[] {
			DailyGoal_tbl.GoalColumn._ID, // 0
			DailyGoal_tbl.GoalColumn.COL_CTN, // 1
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive);
		etGoalContent = (EditText) findViewById(R.id.goal_content_et);

		@SuppressWarnings("deprecation")
		Cursor cursor = managedQuery(getIntent().getData(), // Use the default
				// content URI for
				// the provider.
				PROJECTION, // Return the note ID and content for each note.
				null, // No where clause, return all records.
				null, // No where clause, therefore no where column values.
				DailyGoal_tbl.GoalColumn.DEFAULT_SORT_ORDER // Use the default
															// sort order.
		);

		String[] dataColumns = { DailyGoal_tbl.GoalColumn.COL_CTN };
		int[] viewIDs = { R.id.goal_list };
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, // The
				// Context
				// for the
				// ListView
				R.layout.goal_list_item, // Points to the XML for a list item
				cursor, // The cursor to get items from
				dataColumns, viewIDs);

		// Sets the ListView's adapter to be the cursor adapter that was just
		// created.
		setListAdapter(adapter);

		findViewById(R.id.add_goal_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String goalContent = etGoalContent.getText().toString();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.archive, menu);
		return true;
	}

}
