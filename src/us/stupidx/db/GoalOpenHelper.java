package us.stupidx.db;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import us.stupidx.config.Config;
import us.stupidx.config.DailyGoal_tbl;
import us.stupidx.util.L;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GoalOpenHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 5;

	private SQLiteDatabase db;
	private static GoalOpenHelper goh = null;
	private static final String GOAL_TABLE_CREATE = "CREATE TABLE " + DailyGoal_tbl.TBL_NAME + " ("
			+ DailyGoal_tbl.GoalColumn._ID + " INT, " + DailyGoal_tbl.GoalColumn.COL_DATE
			+ " TEXT, " + DailyGoal_tbl.GoalColumn.COL_CTN + " TEXT, "
			+ DailyGoal_tbl.GoalColumn.COL_CREATE_AT + " TEXT, "
			+ DailyGoal_tbl.GoalColumn.COL_UPDATE_AT + " TEXT, "
			+ DailyGoal_tbl.GoalColumn.COL_FINISH_AT + " TEXT " + ");";

	public GoalOpenHelper(Context context) {
		// calls the super constructor, requesting the default cursor factory.
		super(context, Config.DB_NAME, null, DATABASE_VERSION);
	}
	

	public GoalOpenHelper(Context context, String name, CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(GOAL_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + DailyGoal_tbl.TBL_NAME);
		db.execSQL(GOAL_TABLE_CREATE);
	}

	public Cursor readAll() {
		SQLiteDatabase rDb = this.db = this.getReadableDatabase();

		String[] queryCols = {};
		String selection = null;
		String[] selectionArgs = null;

		Cursor goalListCursor = rDb.query(DailyGoal_tbl.TBL_NAME, queryCols, selection,
				selectionArgs, null, null, DailyGoal_tbl.GoalColumn.COL_DATE + " DESC ");
		return goalListCursor;
	}

	public Cursor readCurrentGoal() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date today = new Date();
		SQLiteDatabase db = this.db = this.getWritableDatabase();

		String[] queryCols = {};
		String selection = DailyGoal_tbl.GoalColumn.COL_DATE + " = ? ";
		String[] selectionArgs = { sdf.format(today) };
		Cursor cursor = db.query(DailyGoal_tbl.TBL_NAME, queryCols, selection, selectionArgs, null,
				null, null);
		return cursor;
	}

	public int updateCurrentGoal(String goalCtn) {
		L.d("updateCurrentGoal", "goalCtn:" + goalCtn);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date today = new Date();
		SQLiteDatabase db = this.db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DailyGoal_tbl.GoalColumn.COL_CTN, goalCtn);
		String whereClause = DailyGoal_tbl.GoalColumn.COL_DATE + " = ? ";
		String[] whereArgs = { sdf.format(today) };
		int updatedCount = db.update(DailyGoal_tbl.TBL_NAME, values, whereClause, whereArgs);
		return updatedCount;
	}

	public long insertCurrentGoal(String goalCtn) {
		L.d("insertCurrentGoal", "goalCtn:" + goalCtn);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date today = new Date();
		SQLiteDatabase db = this.db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DailyGoal_tbl.GoalColumn.COL_DATE, sdf.format(today));
		values.put(DailyGoal_tbl.GoalColumn.COL_CTN, goalCtn);

		long insertedId = db.insert(DailyGoal_tbl.TBL_NAME, "", values);
		return insertedId;
	}

	public void finishiGoal() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date today = new Date();
		SQLiteDatabase db = this.db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DailyGoal_tbl.GoalColumn.COL_DATE, sdf.format(today));
		values.put(DailyGoal_tbl.GoalColumn.COL_FINISH_AT, today.toString());

		String where = DailyGoal_tbl.GoalColumn.COL_DATE + " = ? ";
		String[] params = { sdf.format(today) };
		db.update(DailyGoal_tbl.TBL_NAME, values, where, params);
	}
}
