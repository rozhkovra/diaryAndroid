package smallmazila.diary;

import java.util.Map;

import smallmazila.diary.animation.FlipActivities;
import smallmazila.diary.db.DiaryDbHelper;
import smallmazila.diary.db.TaskProvider;
import smallmazila.diary.framework.CursorFilter;
import smallmazila.diary.framework.DiaryActivity;
import smallmazila.diary.listener.OnTaskTouchListener;
import smallmazila.diary.util.DateUtil;
import smallmazila.diary.view.TaskTouchListView;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TaskListActivity extends DiaryActivity  {

	private static final int IDM_ADD = 102;
	private static final int IDM_MOVE = 103;
	private static final int IDM_SETTINGS = 104;
	private TaskTouchListView mList;
	private Spinner mDirection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mList = (TaskTouchListView)findViewById(R.id.list);
		//mList.setOnTouchListener(new OnTaskTouchListener(getApplicationContext(),mList,cursor));
		
		mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id){
				cursor.filter.mTaskListPosition = position;
				cursor.filter.mTaskId = id;
				startTask();
				if(mClickId != -1){
					mSoundPool.play(mClickId, 1, 1, 0, 0, 1);
				}
			}
		});
		mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id){
				cursor.filter.mTaskListPosition = position;
				Map<String, Object> hm = cursor.mTaskList.get(position);				 
				cursor.filter.mTaskId = cursor.getTaskIdByPosition(position);
				int status = Integer.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_STATUS).toString());
				String text = hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_NAME).toString();
				String statusText = status==1?TaskItem.getStatusName(TaskItem.STATUS_NO):TaskItem.getStatusName(TaskItem.STATUS_YES);				
				ContentValues values =  new ContentValues(2);
				values.put(DiaryDbHelper.TASK_STATUS, 
							status==1?TaskItem.STATUS_NO:TaskItem.STATUS_YES);
				getContentResolver().update(TaskProvider.CONTENT_URI
							, values
							, TaskItem.ID+"="+cursor.filter.mTaskId, null);
				cursor.queryTaskList();
				mList.setAdapter(cursor.getTaskListAdapter());
				Toast.makeText(getApplicationContext(), "Статус задачи "+text.toUpperCase()+" изменен на "+statusText.toUpperCase()+".", Toast.LENGTH_SHORT).show();	
				return true;
			}
		});

		
		mDirection = (Spinner)findViewById(R.id.direction);
		mDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				if(position==0)
					cursor.filter.mCurDirection = -1;
				else{
					Map<String, Object> hm = cursor.mDirectionList.get(position); 					
					cursor.filter.mCurDirection = Integer.valueOf(hm.get(DiaryDbHelper.DIRECTIONS_ID).toString());
				}
				cursor.queryTaskList();
				mList.setAdapter(cursor.getTaskListAdapter());
				changeFilters();
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent){
				
			}
		});
		
		final Button btnYesterday = (Button)findViewById(R.id.yesterday);
		btnYesterday.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cursor.filter.mCurDate = DateUtil.getYesterday(cursor.filter.mCurDate);
				cursor.queryTaskList();
				mList.setAdapter(cursor.getTaskListAdapter());
				changeFilters();
			}
		});
		
		final Button btnTomorrow = (Button)findViewById(R.id.tomorrow);
		btnTomorrow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cursor.filter.mCurDate = DateUtil.getTomorrow(cursor.filter.mCurDate);
				cursor.queryTaskList();
				mList.setAdapter(cursor.getTaskListAdapter());
				changeFilters();
			}
		});
		
		final Button btnToday = (Button)findViewById(R.id.today);
		btnToday.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				cursor.filter.mCurDate = DateUtil.getToday();
				cursor.queryTaskList();
				mList.setAdapter(cursor.getTaskListAdapter());
				changeFilters();
			}
		});
		
		final Button btnAll = (Button)findViewById(R.id.all);
		btnAll.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				cursor.filter.mCurPriority = -1;
				cursor.queryTaskList();
				mList.setAdapter(cursor.getTaskListAdapter());
				changeFilters();
			}
		});
		
		final Button btnPriorityLong = (Button)findViewById(R.id.priority_long);
		btnPriorityLong.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cursor.filter.mCurPriority = TaskItem.PRIORITY_LONG;
				cursor.queryTaskList();
				mList.setAdapter(cursor.getTaskListAdapter());
				changeFilters();
			}
		});
		
		final Button btnImpotantFast = (Button)findViewById(R.id.impotant_fast);
		btnImpotantFast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cursor.filter.mCurPriority = TaskItem.PRIORITY_IMPORTANT_FAST;
				cursor.queryTaskList();
				mList.setAdapter(cursor.getTaskListAdapter());
				changeFilters();
			}
		});

		final Button btnImpotantNoFast = (Button)findViewById(R.id.impotant_nofast);
		btnImpotantNoFast.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cursor.filter.mCurPriority = TaskItem.PRIORITY_IMPORTANT_NOFAST;
				cursor.queryTaskList();
				mList.setAdapter(cursor.getTaskListAdapter());
				changeFilters();
			}
		});
		mFliper.fromLayout = (LinearLayout)findViewById(R.id.layout);
	}
	
	private void changeFilters(){
		final Button btnTomorrow = (Button)findViewById(R.id.tomorrow);
		btnTomorrow.setText(DateUtil.getDate(DateUtil.getTomorrow(cursor.filter.mCurDate))+"("+DateUtil.getDayOfWeekShort(DateUtil.getTomorrow(cursor.filter.mCurDate))+")");
		final Button btnYesterday = (Button)findViewById(R.id.yesterday);
		btnYesterday.setText(DateUtil.getDate(DateUtil.getYesterday(cursor.filter.mCurDate))+"("+DateUtil.getDayOfWeekShort(DateUtil.getYesterday(cursor.filter.mCurDate))+")");
		final TextView curDateTxtView = (TextView)findViewById(R.id.cur_date);
		curDateTxtView.setText(DateUtil.getDate(cursor.filter.mCurDate)+"("+DateUtil.getDayOfWeekShort(cursor.filter.mCurDate)+")");
		TextView curPriority = (TextView)findViewById(R.id.cur_priority);
		curPriority.setText(TaskItem.getPriorityName(cursor.filter.mCurPriority));
		mDirection.setSelection(cursor.filter.mCurDirection);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		menu.add(Menu.NONE, IDM_ADD, Menu.NONE, R.string.menu_task)
			.setIcon(R.drawable.ic_launcher);
		menu.add(Menu.NONE, IDM_MOVE, Menu.NONE, "Move")
			.setIcon(R.drawable.move);
		menu.add(Menu.NONE, IDM_SETTINGS, Menu.NONE, "Settings");
		return (super.onCreateOptionsMenu(menu));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){	
		switch(item.getItemId()){
			case IDM_ADD: {
					cursor.filter.mTaskListPosition = -1;
					startTask();
				}
				break;
			case IDM_MOVE: {
					ContentValues values =  new ContentValues(2);
					values.put(DiaryDbHelper.TASK_ACTUAL_DATE, DateUtil.getDbDate(DateUtil.getToday()));				
					getContentResolver().update(TaskProvider.CONTENT_URI
						, values
						, DiaryDbHelper.TASK_STATUS+"="+0+" and "+DiaryDbHelper.TASK_ACTUAL_DATE + " between '1993-01-01' and '" + DateUtil.getDbDate(cursor.filter.mCurDate)+"'", null);
					cursor.queryTaskList();
					mList.setAdapter(cursor.getTaskListAdapter());
				}
				break;
			case IDM_SETTINGS: {
				startSettings();
			}
			break;
		}
		return (super.onOptionsItemSelected(item));
	}	

	
	@Override
	public void onResume(){
		super.onResume();
		cursor.queryDirections();
		mDirection.setAdapter(cursor.getDirectionSpinnerAdapter(true));
		cursor.queryTaskList();
		mList.setAdapter(cursor.getTaskListAdapter());
		changeFilters();
	}
	
	private void startSettings(){
		Intent intent = new Intent();
		intent.setClass(this, DirectionsActivity.class);
		mFliper.toIntent = intent;
		mFliper.flip();
	}
	
	protected void startTask(){
		Intent intent = new Intent();
		intent.putExtra(CursorFilter.DIARYCURSORFILTER, cursor.filter);
		if(cursor.filter.mTaskListPosition != -1){
			cursor.filter.mTaskId = cursor.getTaskIdByPosition((int)cursor.filter.mTaskListPosition);
			intent.setClass(this, TaskViewActivity.class);
		}
		else{
			cursor.filter.mTaskId = -1;
			intent.setClass(this, TaskActivity.class);
		}
		mFliper.toIntent = intent;
		mFliper.flip();
	}
}
