package us.stupidx.dailygoal;

import us.stupidx.config.Config;
import us.stupidx.db.GoalOpenHelper;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ArchiveActivity extends Activity {

	private EditText etGoalContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive);
		etGoalContent = (EditText) findViewById(R.id.goal_content_et);
		SQLiteOpenHelper dbHelpr = new GoalOpenHelper(this, Config.DB_NAME, null, 1);
		
		findViewById(R.id.add_goal_btn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String goalContent = etGoalContent.getText().toString();
				SQLiteDatabase db = dbHelpr.getWritableDatabase();
				db.insert(Config.GOAL_TABLE_NAME, "", new ContentValues());
				
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
