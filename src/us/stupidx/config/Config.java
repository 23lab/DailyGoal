package us.stupidx.config;

import android.content.Intent;

public final class Config {
	public static final String PREFS_NAME = "DailyGoalSettings";
	public static final int VALID_DRAG_DISTANCE = 100;

	// DB Config
	public static final String DB_NAME = "daily_goal_db";
	public static final String AUTHORITY = "com.google.provider.GoalProvider";
	public static final String MORNING_TIME = "morning_time";
	public static final String AFTERNOON_TIME = "afternoon_time";
	public static final int NTF_ID = 1;
	public static final String ALARM_ACTION = "android.alarm.demo.action";
	public static final String DEFAULT_MORNING_TIME = "9:00";
	public static final String DEFAULT_AFTERNOON_TIME = "18:00";
}
