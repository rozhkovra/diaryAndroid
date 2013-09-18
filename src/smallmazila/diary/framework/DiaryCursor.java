package smallmazila.diary.framework;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import smallmazila.diary.R;
import smallmazila.diary.adapter.TaskListAdapter;
import smallmazila.diary.db.DiaryDbHelper;
import smallmazila.diary.util.DateUtil;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class DiaryCursor {
	public Cursor mDirCursor = null;
	public Cursor mTaskListCursor = null;
	public Cursor mTaskCursor = null;
	private ArrayList <HashMap<String, Object>> mDirectionList = null;
	private SQLiteDatabase mDb;
	public CursorFilter filter;
	private Context context = null;
	private static final String[] mViewContent = new String[]{DiaryDbHelper.TASK_DIRECTIONS_TASK_ID
												, DiaryDbHelper.TASK_DIRECTIONS_TASK_NAME
												, DiaryDbHelper.TASK_DIRECTIONS_TASK_BEGIN_DATE
												, DiaryDbHelper.TASK_DIRECTIONS_TASK_ACTUAL_DATE
												, DiaryDbHelper.TASK_DIRECTIONS_TASK_STATUS
												, DiaryDbHelper.TASK_DIRECTIONS_TASK_PRIORITY
												, DiaryDbHelper.TASK_DIRECTIONS_TASK_USER_ID
												, DiaryDbHelper.TASK_DIRECTIONS_TASK_DEADLINE
												, DiaryDbHelper.TASK_DIRECTIONS_TASK_DIRECTION_ID
												, DiaryDbHelper.TASK_DIRECTIONS_DIRECTIONS_NAME
												, DiaryDbHelper.TASK_DIRECTIONS_DIRECTIONS_STATUS
												, DiaryDbHelper.TASK_DIRECTIONS_DIRECTIONS_USER_ID};
	private static final String mViewOrderBy = DiaryDbHelper.TASK_DIRECTIONS_TASK_STATUS
											+", "+DiaryDbHelper.TASK_DIRECTIONS_TASK_PRIORITY
											+", "+DiaryDbHelper.TASK_DIRECTIONS_TASK_NAME;
	private static final String[] mDirContent = new String[]{DiaryDbHelper._ID
												, DiaryDbHelper.DIRECTIONS_NAME
												, DiaryDbHelper.DIRECTIONS_STATUS
												, DiaryDbHelper.DIRECTIONS_USER_ID};
	
	public DiaryCursor(Context context, CursorFilter filter){
		this.filter=filter;
		this.context = context;
	}
	
	public void queryTaskList(){
		if(mDb==null || !mDb.isOpen())
			mDb =  (new DiaryDbHelper(context)).getWritableDatabase();
		String where = DiaryDbHelper.TASK_DIRECTIONS_TASK_ACTUAL_DATE+"='"+DateUtil.getDbDate(filter.mCurDate)+"'";
		if(filter.mCurPriority>=0)
			where += " and "+DiaryDbHelper.TASK_DIRECTIONS_TASK_PRIORITY+"="+filter.mCurPriority;				
		if(filter.mCurDirection>=0)
			where += " and "+DiaryDbHelper.TASK_DIRECTIONS_TASK_DIRECTION_ID+"="+filter.mCurDirection;
		mTaskListCursor = mDb.query(DiaryDbHelper.VIEW_TASK_DIRECTION, mViewContent, where, null, null, null, mViewOrderBy);
	}
	
	public void queryTask(){
		if(mDb==null || !mDb.isOpen())
			mDb =  (new DiaryDbHelper(context)).getWritableDatabase();
		mTaskCursor = mDb.query(DiaryDbHelper.VIEW_TASK_DIRECTION, mViewContent, DiaryDbHelper.TASK_DIRECTIONS_TASK_ID+"="+filter.mTaskId, null, null, null, mViewOrderBy);
	}
		
	public void queryDirections(){
		if(mDirCursor!=null && !mDirCursor.isClosed())
			mDirCursor.close();
		if(mDb==null || !mDb.isOpen())
			mDb =  (new DiaryDbHelper(context)).getWritableDatabase();
		if(mDirectionList == null)
			mDirectionList = new ArrayList<HashMap<String,Object>>();
		else
			mDirectionList.clear();
		mDirCursor = mDb.query(DiaryDbHelper.TABLE_DIRECTIONS, mDirContent, DiaryDbHelper.DIRECTIONS_STATUS+"=1", null, null, null, null);
		HashMap<String, Object> hm;
		hm = new HashMap<String, Object>();
		hm.put(DiaryDbHelper.DIRECTIONS_NAME, "Все");
		mDirectionList.add(hm);

		if(mDirCursor.moveToFirst()){
			do{
				hm = new HashMap<String, Object>();
				hm.put(DiaryDbHelper.DIRECTIONS_NAME, mDirCursor.getString(1));
	            mDirectionList.add(hm);
			}while(mDirCursor.moveToNext());
		}
	}
	
	public ListAdapter getTaskListAdapter(){
		return new TaskListAdapter(context
					, R.layout.task_list_item
					, mTaskListCursor
					, new String[]{DiaryDbHelper.TASK_DIRECTIONS_TASK_NAME, DiaryDbHelper.TASK_DIRECTIONS_DIRECTIONS_NAME, DiaryDbHelper.TASK_DIRECTIONS_TASK_DEADLINE, DiaryDbHelper.TASK_DIRECTIONS_TASK_NAME}
					, new int[]{R.id.name, R.id.direction_name, R.id.deadline, R.id.detail}
					);
	}
	
	public SimpleAdapter getDirectionAdapter(boolean withAll){
		if(!withAll){
			HashMap<String, Object> hm = mDirectionList.get(0);
			if(hm!=null && "Все".equals(hm.get(DiaryDbHelper.DIRECTIONS_NAME)))
				mDirectionList.remove(0);			
		}
		SimpleAdapter adapter = new SimpleAdapter(context
				, mDirectionList 
				, android.R.layout.simple_spinner_item 										
				, new String[]{DiaryDbHelper.DIRECTIONS_NAME}
				, new int[]{android.R.id.text1}
		);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
	}
	
	public void close(){
		if(mTaskListCursor!=null && !mTaskListCursor.isClosed())
			mTaskListCursor.close();
		if(mDirCursor!=null && !mDirCursor.isClosed())
			mDirCursor.close();
		if(mDb!=null && mDb.isOpen())
			mDb.close();
	}
}
