package us.stupidx.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class GoalOpenHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;
	private static final String GOAL_TABLE_NAME = "daily_goal";
	
	
	private static final String COL_DATE = "g_date";
	private static final String COL_GOAL_CONTENT = "g_ctn";
	private static final String COL_CREATE_AT = "g_create_at";
	private static final String COL_UPDATE_AT = "g_update_at";
	private static final String GOAL_TABLE_CREATE = "CREATE TABLE "
			+ GOAL_TABLE_NAME + " (" + COL_DATE + " TEXT, "
			+ COL_GOAL_CONTENT + " TEXT);";

	public GoalOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public GoalOpenHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(GOAL_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
