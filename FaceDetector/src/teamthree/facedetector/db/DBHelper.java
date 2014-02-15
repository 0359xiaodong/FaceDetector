package teamthree.facedetector.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String NAME_TABLE = "FacebookUsers";
	public static final String KEY_COLUMN_ID = "_id";
	public static final String KEY_COLUMN_NAME = "name";
	public static final String KEY_COLUMN_PICTURE = "picture";

	private static final String CREATE_TABLE = "CREATE TABLE " + NAME_TABLE
			+ " (" + KEY_COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, "
			+ KEY_COLUMN_NAME + " TEXT, " + KEY_COLUMN_PICTURE + " TEXT)";

	private static final String DROP_TABLE = "DROP TABLE IF EXISTS "
			+ NAME_TABLE;

	private static final int DB_VERSION = 4;
	private static final String DB_NAME = "content.db";

	private static DBHelper mDbHelper = null;

	public DBHelper(Context context) {

		super(context, DB_NAME, null, DB_VERSION);

	}

	public synchronized static DBHelper getInstance(Context context) {

		if (mDbHelper == null)
			mDbHelper = new DBHelper(context);

		return mDbHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL(DROP_TABLE);
	}
}
