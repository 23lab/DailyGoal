package us.stupidx.dailygoal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import us.stupidx.config.Config;
import us.stupidx.config.DailyGoal_tbl;
import us.stupidx.db.GoalOpenHelper;
import android.app.Activity;
import android.app.NotificationManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.squareup.timessquare.CalendarPickerView;
import com.squareup.timessquare.CalendarPickerView.DateSelectableFilter;
import com.squareup.timessquare.CalendarPickerView.FluentInitializer;
import com.squareup.timessquare.CalendarPickerView.OnDateSelectedListener;
import com.squareup.timessquare.CalendarPickerView.SelectionMode;

public class ArchiveActivity extends Activity {
	private GoalOpenHelper openHelper;
	private Cursor goalListCursor;
	private Button rtnBtn;
	private HashMap<Date, String> dateGoals;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive);
		openHelper = new GoalOpenHelper(this);
		// etGoalContent = (EditText) findViewById(R.id.goal_content);

		this.fillGoal();

		// findViewById(R.id.add_goal_btn).setOnClickListener(new AddGoalListener());

		// GestureOverlayView gov = (GestureOverlayView) findViewById(R.id.archive_gesture_ov);
		// gov.setGestureVisible(true);
		// gov.addOnGestureListener(new NavGestureListener(this, null, HomeActivity.class));

		rtnBtn = (Button) findViewById(R.id.archive_return_btn);

		rtnBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ArchiveActivity.this.finish();
				ArchiveActivity.this.overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
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

	private void fillGoal() {
		Calendar nextYear = Calendar.getInstance();
		nextYear.add(Calendar.YEAR, 1);

		Calendar preYear = Calendar.getInstance();
		preYear.add(Calendar.YEAR, -1);

		Calendar c = new GregorianCalendar();
		c.setTime(new Date());
		c.add(Calendar.DATE, 1);

		CalendarPickerView calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
		FluentInitializer fi = calendar.init(preYear.getTime(), c.getTime()).inMode(SelectionMode.MULTIPLE);

		// 从数据库读取所有goals
		goalListCursor = openHelper.readAll();
		dateGoals = new HashMap<Date, String>();
		goalListCursor.moveToFirst();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		while (!goalListCursor.isAfterLast()) {
			try {
				Date date = format.parse(goalListCursor.getString(goalListCursor
						.getColumnIndex(DailyGoal_tbl.GoalColumn.COL_DATE)));

				// c.setTime(new Date());
				//c.add(Calendar.DATE, -4);
				calendar.selectDate(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			goalListCursor.moveToNext();
		}
		calendar.setStackFromBottom(true);
		calendar.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		calendar.setDateSelectableFilter(new DateSelectableFilter() {
			@Override
			public boolean isDateSelectable(Date date) {
				return false;
			}
		});

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
