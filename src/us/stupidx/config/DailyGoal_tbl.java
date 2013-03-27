package us.stupidx.config;

import android.net.Uri;
import android.provider.BaseColumns;

public class DailyGoal_tbl {
	public static final String TBL_NAME = "daily_goal_tb";
	public static final String AUTHORITY = "com.google.provider.GoalProvider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/goals");

	public static final class GoalColumn implements BaseColumns {
		public static final int GOAL_ID_PATH_POSITION = 1;

		public static final String COL_DATE = "g_date";
		public static final String COL_CTN = "g_ctn";
		public static final String COL_CREATE_AT = "g_create_at";
		public static final String COL_UPDATE_AT = "g_update_at";
		
		public static final String COL_FINISH_AT = "g_finish_at";

		public static final String DEFAULT_SORT_ORDER = "modified DESC";

	}
}
