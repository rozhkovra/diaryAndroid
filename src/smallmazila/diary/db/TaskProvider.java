package smallmazila.diary.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class TaskProvider extends ContentProvider{
	public static final Uri CONTENT_URI = Uri.parse(
			"content://smallmazila.diary.db.taskprovider/task"
			);
	public static final int URI_CODE = 1;
	public static final int URI_CODE_ID = 2;

	private SQLiteDatabase db;
	
	@Override
	public boolean onCreate(){
		db = (new DiaryDbHelper(getContext())).getWritableDatabase();
		return (db != null)?false:true;
	}
	
	@Override
	public Cursor query(Uri url, String[] projection,
			String selection, String[] selectionArgs, String sort){
		String orderBy;
		if (TextUtils.isEmpty(sort))
			orderBy = DiaryDbHelper.TASK_NAME;
		else
			orderBy = sort;
		
		Cursor c = db.query(DiaryDbHelper.TABLE_TASK
				, projection, selection, selectionArgs,
				null, null, orderBy);
		
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}
	
	@Override
	public Uri insert(Uri url, ContentValues inValues){
		ContentValues values = new ContentValues(inValues);
		
		long rowId = db.insert(DiaryDbHelper.TABLE_TASK, DiaryDbHelper.TASK_NAME, values);
		
		if(rowId > 0){
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}else
			throw new SQLException("Failed to insert row into " + url);
	}
	
	@Override
	public int delete(Uri url, String where, String[] whereArgs){
		int retVal = db.delete(DiaryDbHelper.TABLE_TASK, where, whereArgs);
		
		getContext().getContentResolver().notifyChange(url, null);
		return retVal;
	}

	@Override
	public int update(Uri url, ContentValues values ,String where, String[] whereArgs){
		int retVal = db.update(DiaryDbHelper.TABLE_TASK, values, where, whereArgs);
		getContext().getContentResolver().notifyChange(url, null);
		return retVal;
	}
	
	@Override
	public String getType(Uri uri){
		return null;
	}

}
