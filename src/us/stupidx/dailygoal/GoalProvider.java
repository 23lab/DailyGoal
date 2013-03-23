package us.stupidx.dailygoal;

import java.util.HashMap;
import java.util.Map;

import us.stupidx.config.Config;
import us.stupidx.config.DailyGoal_tbl;
import us.stupidx.db.GoalOpenHelper;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class GoalProvider extends ContentProvider {
	private static UriMatcher sURIMatcher;
	private static final int GOALS = 1;
	private static final int GOAL_ID = 2;

	private static Map<String, String> sNotesProjectionMap;
	private GoalOpenHelper mOpenHelper;

	static {
		sURIMatcher.addURI(Config.AUTHORITY, "goals", GOALS);
		sURIMatcher.addURI(Config.AUTHORITY, "goal/#", GOAL_ID);

		sNotesProjectionMap = new HashMap<String, String>();

		// Maps the string "_ID" to the column name "_ID"
		sNotesProjectionMap.put(DailyGoal_tbl.GoalColumn._ID,
				DailyGoal_tbl.GoalColumn._ID);
		// Maps "g_date" to "g_date"
		sNotesProjectionMap.put(DailyGoal_tbl.GoalColumn.COL_DATE,
				DailyGoal_tbl.GoalColumn.COL_DATE);
		// Maps "note" to "note"
		sNotesProjectionMap.put(DailyGoal_tbl.GoalColumn.COL_CTN,
				DailyGoal_tbl.GoalColumn.COL_CTN);
		// Maps "create_at" to "create_at"
		sNotesProjectionMap.put(DailyGoal_tbl.GoalColumn.COL_CREATE_AT,
				DailyGoal_tbl.GoalColumn.COL_CREATE_AT);
		// Maps "update_at" to "update_at"
		sNotesProjectionMap.put(DailyGoal_tbl.GoalColumn.COL_UPDATE_AT,
				DailyGoal_tbl.GoalColumn.COL_UPDATE_AT);
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new GoalOpenHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(DailyGoal_tbl.TBL_NAME);

		switch (sURIMatcher.match(uri)) {
		// If the incoming URI is for notes, chooses the Notes projection
		case GOALS:
			qb.setProjectionMap(sNotesProjectionMap);
			break;

		/*
		 * If the incoming URI is for a single note identified by its ID,
		 * chooses the note ID projection, and appends "_ID = <noteID>" to the
		 * where clause, so that it selects that single note
		 */
		case GOAL_ID:
			qb.setProjectionMap(sNotesProjectionMap);
			qb.appendWhere(DailyGoal_tbl.GoalColumn._ID + // the name of the ID
															// column
					"=" +
					// the position of the note ID itself in the incoming URI
					uri.getPathSegments().get(
							DailyGoal_tbl.GoalColumn.GOAL_ID_PATH_POSITION));
			break;

		}

		String orderBy;
		// If no sort order is specified, uses the default
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = DailyGoal_tbl.GoalColumn.DEFAULT_SORT_ORDER;
		} else {
			// otherwise, uses the incoming sort order
			orderBy = sortOrder;
		}
		// Opens the database object in "read" mode, since no writes need to be
		// done.
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, // The database to query
				projection, // The columns to return from the query
				selection, // The columns for the where clause
				selectionArgs, // The values for the where clause
				null, // don't group the rows
				null, // don't filter by row groups
				orderBy // The sort order
				);

		// Tells the Cursor what URI to watch, so it knows when its source data
		// changes
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public String getType(Uri uri) {
		int match = sURIMatcher.match(uri);
		switch (match) {
		case GOALS:
			return "vnd.android.cursor.dir/goals";
		case GOAL_ID:
			return "vnd.android.cursor.item/goal";
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
