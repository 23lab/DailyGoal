package us.stupidx.dailygoal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import us.stupidx.config.Config;
import us.stupidx.config.DailyGoal_tbl;
import us.stupidx.db.GoalOpenHelper;
import us.stupidx.util.CT;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ext.IDegreeProvider;
import android.view.ext.SatelliteMenu;
import android.view.ext.SatelliteMenu.SateliteClickedListener;
import android.view.ext.SatelliteMenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private GoalOpenHelper openHelper;
	private CheckBox cbTodayGoal;

	private char[] week = { '日', '一', '二', '三', '四', '五', '六' };
	private Button archiveBtn;
	private Button settingBtn;
	protected View dlgView;
	private SatelliteMenu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		this.chenckInitConfig();
		openHelper = new GoalOpenHelper(this);

		this.findViewById();
		this.setListener();

		this.createSatelliteMenu();
		// 设置闹铃
		this.setAlarmTime(this, 1000);

	}

	@SuppressWarnings("deprecation")
	private void renderHomeView() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
		Date today = new Date();
		((TextView) findViewById(R.id.today_date_tv)).setText(sdf.format(today));
		((TextView) findViewById(R.id.today_day_tv)).setText(("星期" + week[today.getDay()]));

		// 没有设定目标时, 这里应该是不可以选中的
		if (!CT.isEmpty(cbTodayGoal.getText().toString())) {
			cbTodayGoal.setEnabled(false);
		}

		Cursor cursor = openHelper.readCurrentGoal();
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			String ctn = cursor.getString(cursor.getColumnIndex(DailyGoal_tbl.GoalColumn.COL_CTN));
			String finishiAt = cursor.getString(cursor
					.getColumnIndex(DailyGoal_tbl.GoalColumn.COL_FINISH_AT));
			if (finishiAt != null) {
				cbTodayGoal.setChecked(true);
				cbTodayGoal.setEnabled(false);
			}
			cbTodayGoal.setText(ctn);
		}

		if (openHelper.readCurrentGoal().getCount() == 0) { // 如果没有设置目标, 则弹出添加对话框
			popAddGoalDlg();
		}

	}

	private void createSatelliteMenu() {
		menu = (SatelliteMenu) findViewById(R.id.menu);
		float distance = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, getResources()
				.getDisplayMetrics());
		menu.setSatelliteDistance((int) distance);
		menu.setExpandDuration(500);
		menu.setCloseItemsOnClick(true);
		menu.setTotalSpacingDegree(70);
		menu.setAnimationCacheEnabled(false);
		menu.setGapDegreeProvider(new IDegreeProvider() {
			@Override
			public float[] getDegrees(int count, float totalDegrees) {
				float[] degrees = new float[count];
				for (int i = 0; i < count; i++) {
					degrees[i] = 90 + (90 - totalDegrees) / 2 + totalDegrees / (count - 1) * i;
				}
				return degrees;
			}
		});
		// menu.setSoundEffectsEnabled(true);

		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
		items.add(new SatelliteMenuItem(3, R.drawable.ic_5));
		items.add(new SatelliteMenuItem(2, R.drawable.ic_6));
		items.add(new SatelliteMenuItem(1, R.drawable.ic_2));
		menu.addItems(items);
		menu.setOnItemClickedListener(new SateliteClickedListener() {
			public void eventOccured(int id) {
				Log.i("sat", "Clicked on " + id);
				switch (id) {
				case 1:
					HomeActivity.this.startActivity(new Intent(HomeActivity.this,
							ArchiveActivity.class));
					HomeActivity.this.overridePendingTransition(R.anim.push_right_in,
							R.anim.push_right_out);
					break;
				case 2:
					popAddGoalDlg();
					break;
				case 3:
					HomeActivity.this.startActivity(new Intent(HomeActivity.this,
							SettingsActivity.class));
					HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
							R.anim.push_left_out);
					break;
				}
			}
		});
	}

	private void chenckInitConfig() {
		SharedPreferences settings = this.getSharedPreferences(Config.PREFS_NAME, 0);

		// 如果app还未设置时间
		if (settings.getString(Config.MORNING_TIME, "").equals("")) {
			Editor editSetting = settings.edit();
			editSetting.putString(Config.MORNING_TIME, Config.DEFAULT_MORNING_TIME);
			editSetting.putString(Config.AFTERNOON_TIME, Config.DEFAULT_AFTERNOON_TIME);
			editSetting.commit();
		}
	}

	private void findViewById() {
		settingBtn = (Button) findViewById(R.id.setting_btn);
		archiveBtn = (Button) findViewById(R.id.archive_btn);
		cbTodayGoal = (CheckBox) findViewById(R.id.today_goal_cb);
	}

	private void setListener() {
		settingBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				HomeActivity.this.startActivity(new Intent(HomeActivity.this,
						SettingsActivity.class));
				HomeActivity.this.overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		});

		archiveBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				HomeActivity.this
						.startActivity(new Intent(HomeActivity.this, ArchiveActivity.class));
				HomeActivity.this.overridePendingTransition(R.anim.push_right_in,
						R.anim.push_right_out);
			}
		});

		cbTodayGoal.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if (arg1) {
					Toast.makeText(HomeActivity.this, "Yes", Toast.LENGTH_SHORT).show();
					openHelper.finishiGoal();
				} else {
					Toast.makeText(HomeActivity.this, "No", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	private void setAlarmTime(Context context, long timeInMillis) {
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(Config.ALARM_ACTION);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		int interval = 60 * 1000;// 闹铃间隔， 这里设为1分钟闹一次，在第2步我们将每隔1分钟收到一次广播]
		am.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, interval, sender);
	}

	private void popAddGoalDlg() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// Add the buttons
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				EditText cfmEt = (EditText) dlgView.findViewById(R.id.dlg_goal_txt_et);
				String goalTxt = cfmEt.getText().toString();
				if (CT.notEmpty(goalTxt)) {
					Cursor cursor = openHelper.readCurrentGoal();
					if (cursor.getCount() > 0) {
						openHelper.updateCurrentGoal(goalTxt);
					} else {
						openHelper.insertCurrentGoal(goalTxt);
					}
				}

				HomeActivity.this.renderHomeView();
			}
		});
		// Set other dialog properties
		builder.setTitle(R.string.today_s_goal);
		LayoutInflater inflater = this.getLayoutInflater();
		dlgView = inflater.inflate(R.layout.dlg_add_goal, null);
		builder.setView(dlgView);
		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.renderHomeView();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (openHelper != null) {
			openHelper.close();
		}
	}

}
