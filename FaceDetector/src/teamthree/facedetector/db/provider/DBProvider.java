package teamthree.facedetector.db.provider;

import teamthree.facedetector.db.DBHelper;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DBProvider extends ContentProvider {

	private static final int URI_DATA = 1;

	private static final String AUTHORITY = "teamthree.facedetector.contentprovider";

	private static final String BASE_PATH_DATA = "data";

	public static final Uri CONTENT_DATA_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + BASE_PATH_DATA);

	public static final String CONTENT_TYPE_DATA = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/data";

	private final UriMatcher mUriMatcher;

	private static DBHelper mDBHelper = null;
	private static SQLiteDatabase mSqLiteDatabase = null;

	public DBProvider() {

		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(AUTHORITY, BASE_PATH_DATA, URI_DATA);
	}

	@Override
	public boolean onCreate() {

		mDBHelper = DBHelper.getInstance(getContext());
		mSqLiteDatabase = mDBHelper.getWritableDatabase();

		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		Cursor mCursor = null;

		switch (mUriMatcher.match(uri)) {

		case URI_DATA:

			mCursor = mSqLiteDatabase.query(DBHelper.NAME_TABLE, null, null,
					null, null, null, null);
			break;

		default:

			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		mCursor.setNotificationUri(getContext().getContentResolver(), uri);

		return mCursor;
	}

	@Override
	public String getType(Uri uri) {

		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		return 0;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		long id = 0;
		switch (mUriMatcher.match(uri)) {

		case URI_DATA:

			id = mSqLiteDatabase.insert(DBHelper.NAME_TABLE, null, values);
			break;

		default:

			throw new IllegalArgumentException("Wrong URI: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH_DATA + "/" + id);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		return 0;
	}
}