package us.stupidx.config;

import android.provider.BaseColumns;

public class DailyGoal_tbl {
	public static final String TBL_NAME = "daily_goal_tb";
	
	
	
	public static final class GoalColumn implements BaseColumns {
		public static final int GOAL_ID_PATH_POSITION = 1;
		
		public static final String COL_DATE = "g_date";
		public static final String COL_CTN = "g_ctn";
		public static final String COL_CREATE_AT = "g_create_at";
		public static final String COL_UPDATE_AT = "g_update_at";

		public static final String DEFAULT_SORT_ORDER = "modified DESC";
	}
}
