package smallmazila.diary.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class DirectionProvider extends ContentProvider {
	public static final Uri CONTENT_URI = Uri.parse(
			"content://smallmazila.diary.db.directionprovider/directions"
			);
	public static final int URI_CODE = 1;
	public static final int URI_CODE_ID = 2;

	private SQLiteDatabase db;
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int retVal = db.delete(DiaryDbHelper.TABLE_DIRECTIONS, selection, selectionArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		return retVal;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues inValues) {
		ContentValues values = new ContentValues(inValues);
		
		long rowId = db.insert(DiaryDbHelper.TABLE_DIRECTIONS, DiaryDbHelper.DIRECTIONS_NAME, values);
		
		if(rowId > 0){
			Uri url = ContentUris.withAppendedId(CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(url, null);
			return uri;
		}else
			throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		db = (new DiaryDbHelper(getContext())).getWritableDatabase();
		return (db != null)?false:true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		// TODO Auto-generated method stub
		String orderBy;
		if (TextUtils.isEmpty(sort))
			orderBy = DiaryDbHelper.DIRECTIONS_NAME;
		else
			orderBy = sort;
		
		Cursor c = db.query(DiaryDbHelper.TABLE_DIRECTIONS
				, projection, selection, selectionArgs,
				null, null, orderBy);
		
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int retVal = db.update(DiaryDbHelper.TABLE_TASK, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return retVal;
	}

}
