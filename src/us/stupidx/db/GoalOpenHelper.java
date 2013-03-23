package us.stupidx.db;

import us.stupidx.config.Config;
import us.stupidx.config.DailyGoal_tbl;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GoalOpenHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 2;

	private static final String GOAL_TABLE_CREATE = "CREATE TABLE "
			+ DailyGoal_tbl.TBL_NAME + " (" + DailyGoal_tbl.GoalColumn.COL_DATE
			+ " TEXT, " + DailyGoal_tbl.GoalColumn.COL_CTN + " TEXT);";

	public GoalOpenHelper(Context context) {
        // calls the super constructor, requesting the default cursor factory.
        super(context, Config.DB_NAME, null, DATABASE_VERSION);
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
