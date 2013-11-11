package smallmazila.diary.framework;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import smallmazila.diary.R;
import smallmazila.diary.TaskItem;
import smallmazila.diary.adapter.TaskListSimpleAdapter;
import smallmazila.diary.db.DiaryDbHelper;
import smallmazila.diary.db.TaskProvider;
import smallmazila.diary.util.DateUtil;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class DiaryCursor {
	public ArrayList <HashMap<String, Object>> mDirectionList = null;
	public ArrayList <HashMap<String, Object>> mTaskList = null;
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
		if(mTaskList == null)
			mTaskList = new ArrayList<HashMap<String,Object>>();
		else
			mTaskList.clear();
		SQLiteDatabase db =  (new DiaryDbHelper(context)).getWritableDatabase();
		String where = DiaryDbHelper.TASK_DIRECTIONS_TASK_ACTUAL_DATE+"='"+DateUtil.getDbDate(filter.mCurDate)+"'";
		if(filter.mCurPriority>=0)
			where += " and "+DiaryDbHelper.TASK_DIRECTIONS_TASK_PRIORITY+"="+filter.mCurPriority;				
		if(filter.mCurDirection>=0)
			where += " and "+DiaryDbHelper.TASK_DIRECTIONS_TASK_DIRECTION_ID+"="+filter.mCurDirection;
		Cursor cursor = db.query(DiaryDbHelper.VIEW_TASK_DIRECTION, mViewContent, where, null, null, null, mViewOrderBy);
		HashMap<String, Object> hm;
		if(cursor.moveToFirst()){
			do{
				hm = new HashMap<String, Object>();
				hm.put(DiaryDbHelper.TASK_DIRECTIONS_TASK_ID, cursor.getLong(0));
				hm.put(DiaryDbHelper.TASK_DIRECTIONS_TASK_NAME, cursor.getString(1));
				hm.put(DiaryDbHelper.TASK_DIRECTIONS_TASK_BEGIN_DATE, cursor.getString(2));
				hm.put(DiaryDbHelper.TASK_DIRECTIONS_TASK_ACTUAL_DATE, cursor.getString(3));
				hm.put(DiaryDbHelper.TASK_DIRECTIONS_TASK_STATUS, cursor.getInt(4));
				hm.put(DiaryDbHelper.TASK_DIRECTIONS_TASK_PRIORITY, cursor.getInt(5));
				hm.put(DiaryDbHelper.TASK_DIRECTIONS_TASK_USER_ID, cursor.getInt(6));
				hm.put(DiaryDbHelper.TASK_DIRECTIONS_TASK_DEADLINE, cursor.getString(7));
				hm.put(DiaryDbHelper.TASK_DIRECTIONS_TASK_DIRECTION_ID, cursor.getLong(8));
				hm.put(DiaryDbHelper.TASK_DIRECTIONS_DIRECTIONS_NAME, cursor.getString(9));
				hm.put(DiaryDbHelper.TASK_DIRECTIONS_DIRECTIONS_STATUS, cursor.getInt(10));
				hm.put(DiaryDbHelper.TASK_DIRECTIONS_DIRECTIONS_USER_ID, cursor.getInt(11));
	            mTaskList.add(hm);
			}while(cursor.moveToNext());
		}
		cursor.close();
		db.close();
	}
	
	public void queryDirections(){
		SQLiteDatabase db =  (new DiaryDbHelper(context)).getWritableDatabase();
		if(mDirectionList == null)
			mDirectionList = new ArrayList<HashMap<String,Object>>();
		else
			mDirectionList.clear();
		Cursor cursor = db.query(DiaryDbHelper.TABLE_DIRECTIONS, mDirContent, DiaryDbHelper.DIRECTIONS_STATUS+"=1", null, null, null, null);
		HashMap<String, Object> hm;
		hm = new HashMap<String, Object>();
		hm.put(DiaryDbHelper.DIRECTIONS_ID, -1);
		hm.put(DiaryDbHelper.DIRECTIONS_NAME, "Все");
		mDirectionList.add(hm);

		if(cursor.moveToFirst()){
			do{
				hm = new HashMap<String, Object>();
				hm.put(DiaryDbHelper.DIRECTIONS_ID, cursor.getLong(0));
				hm.put(DiaryDbHelper.DIRECTIONS_NAME, cursor.getString(1));
	            mDirectionList.add(hm);
			}while(cursor.moveToNext());
		}
		cursor.close();
		db.close();		
	}
	
	public ListAdapter getTaskListAdapter(){
		return new TaskListSimpleAdapter(context
				, mTaskList 
				, R.layout.task_list_item 										
				, new String[]{DiaryDbHelper.TASK_DIRECTIONS_TASK_NAME, DiaryDbHelper.TASK_DIRECTIONS_DIRECTIONS_NAME, DiaryDbHelper.TASK_DIRECTIONS_TASK_DEADLINE, DiaryDbHelper.TASK_DIRECTIONS_TASK_NAME}
				, new int[]{R.id.name, R.id.direction_name, R.id.deadline, R.id.detail}
		);
	}
	
	public SimpleAdapter getDirectionSpinnerAdapter(boolean withAll){
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

	public SimpleAdapter getDirectionListAdapter(){
		return new SimpleAdapter(context
				, mDirectionList 
				, R.layout.direction_item 										
				, new String[]{DiaryDbHelper.DIRECTIONS_NAME}
				, new int[]{R.id.name}
		);
	}
	
	public long getTaskIdByPosition(int position){
		Map<String, Object> hm = mTaskList.get(position);				 
		return Long.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_ID).toString());
	}

	public int getTaskPositionByTaskId(long taskId){
		int position = -1;
		int counter = -1;
		for(Map<String, Object> hm : mTaskList){
			counter++;
			if(Long.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_ID).toString()).equals(taskId))
				position=counter;
		}
		return position;
	}
	
	public void taskNextDay(int pos) {
		// TODO Auto-generated method stub
		Map<String, Object> hm = mTaskList.get(pos);
		String actualDate = hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_ACTUAL_DATE).toString();
		try{
			Date d = DateUtil.getDate(DateUtil.getClientDate(actualDate));
			d = DateUtil.plusDays(d, 1);
			ContentValues values =  new ContentValues(2);
			values.put(DiaryDbHelper.TASK_ACTUAL_DATE, 
						DateUtil.getDbDate(d));
			context.getContentResolver().update(TaskProvider.CONTENT_URI
						, values
						, TaskItem.ID+"="+hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_ID).toString(), null);
		}catch(Exception e){
			
		}
	}
	
	public void taskPrevDay(int pos) {
		// TODO Auto-generated method stub
		Map<String, Object> hm = mTaskList.get(pos);
		String actualDate = hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_ACTUAL_DATE).toString();
		try{
			Date d = DateUtil.getDate(DateUtil.getClientDate(actualDate));
			d = DateUtil.plusDays(d, -1);
			ContentValues values =  new ContentValues(2);
			values.put(DiaryDbHelper.TASK_ACTUAL_DATE, 
						DateUtil.getDbDate(d));
			context.getContentResolver().update(TaskProvider.CONTENT_URI
						, values
						, TaskItem.ID+"="+hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_ID).toString(), null);
		}catch(Exception e){
			
		}
	}

	public void taskLower(int pos) {
		// TODO Auto-generated method stub
		Map<String, Object> hm = mTaskList.get(pos);
		int priority = Integer.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_PRIORITY).toString());
		if(priority!=TaskItem.PRIORITY_IMPORTANT_NOFAST){
			ContentValues values =  new ContentValues(2);
			values.put(DiaryDbHelper.TASK_PRIORITY, 
						priority-1);
			context.getContentResolver().update(TaskProvider.CONTENT_URI
						, values
						, TaskItem.ID+"="+hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_ID).toString(), null);
		}
	}
	
	public void taskHigher(int pos) {
		// TODO Auto-generated method stub
		Map<String, Object> hm = mTaskList.get(pos);
		int priority = Integer.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_PRIORITY).toString());
		if(priority!=TaskItem.PRIORITY_LONG){
			ContentValues values =  new ContentValues(2);
			values.put(DiaryDbHelper.TASK_PRIORITY, 
						priority+1);
			context.getContentResolver().update(TaskProvider.CONTENT_URI
						, values
						, TaskItem.ID+"="+hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_ID).toString(), null);
		}
	}
}
