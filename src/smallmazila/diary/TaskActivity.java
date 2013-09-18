package smallmazila.diary;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import smallmazila.diary.animation.FlipActivities;
import smallmazila.diary.db.DiaryDbHelper;
import smallmazila.diary.db.TaskProvider;
import smallmazila.diary.framework.CursorFilter;
import smallmazila.diary.framework.DiaryActivity;
import smallmazila.diary.framework.DiaryCursor;
import smallmazila.diary.util.DateUtil;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class TaskActivity extends DiaryActivity{
	private EditText mName;
	private CheckBox mStatus;
	private TextView mBeginDate;
	private TextView mActualDate;
	private Spinner mPriority;
	private Spinner mDirection;
	private TextView mDeadline;
	static final int ACTUAL_DATE_DIALOG_ID = 1;
	static final int DEADLINE_DIALOG_ID = 2;
	private DiaryCursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_content);

		mName = (EditText)findViewById(R.id.task_name);
		mBeginDate = (TextView)findViewById(R.id.begin_date_name);		
		mActualDate = (TextView)findViewById(R.id.actual_date_name);
		mActualDate.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog(ACTUAL_DATE_DIALOG_ID);	
			}
		});
		mStatus = (CheckBox)findViewById(R.id.status_name);
		mPriority = (Spinner)findViewById(R.id.priority_name);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, 
				TaskItem.priorityValues());
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mPriority.setAdapter(arrayAdapter);

		mDeadline = (TextView)findViewById(R.id.deadline_name);
		mDeadline.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog(DEADLINE_DIALOG_ID);	
			}
		});
		mDirection  = (Spinner)findViewById(R.id.direction_name);
		
		cursor = new DiaryCursor(getApplicationContext(),new CursorFilter());
		Bundle extras = getIntent().getExtras();
		cursor.filter = (CursorFilter)extras.get(CursorFilter.DIARYCURSORFILTER);
		cursor.queryTaskList();
		cursor.queryDirections();

		fillForm();
		
		final Button btnOk = (Button)findViewById(R.id.button_save);		
		btnOk.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				save();
				mFliper.finish();
				if(mClickId != -1){
					mSoundPool.play(mClickId, 1, 1, 0, 0, 1);
				}
			}
		});
		
		final Button btnCancel = (Button)findViewById(R.id.button_cancel);		
		btnCancel.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				mFliper.finish();
				if(mClickId != -1){
					mSoundPool.play(mClickId, 1, 1, 0, 0, 1);
				}
			}
		});
		mFliper = new FlipActivities(this, (LinearLayout)findViewById(R.id.task), null);
		mFliper.initialize();
	}
	
	
	private DatePickerDialog.OnDateSetListener mActualDateSetListener = 
			new DatePickerDialog.OnDateSetListener() {			
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					mActualDate.setText(DateUtil.getDate(year, monthOfYear,dayOfMonth));
				}
			};
			
	private DatePickerDialog.OnDateSetListener mDeadlineSetListener = 
			new DatePickerDialog.OnDateSetListener() {			
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {
					mDeadline.setText(DateUtil.getDate(year, monthOfYear,dayOfMonth));
				}
			};		
			
	@Override
	protected Dialog onCreateDialog(int id){
		switch(id){
		case ACTUAL_DATE_DIALOG_ID:
			try{
				Date actualDate = DateUtil.getDate(mActualDate.getText().toString());
				DateUtil.c.setTime(actualDate);
				return new DatePickerDialog(this,
						mActualDateSetListener,
						DateUtil.c.get(Calendar.YEAR), DateUtil.c.get(Calendar.MONTH), DateUtil.c.get(Calendar.DAY_OF_MONTH));
			}catch(Exception e){
			}
			break;
		case DEADLINE_DIALOG_ID:
			try{
				Date deadlineDate = DateUtil.getDate(mDeadline.getText().toString());
				DateUtil.c.setTime(deadlineDate);
				return new DatePickerDialog(this,
						mDeadlineSetListener,
					DateUtil.c.get(Calendar.YEAR), DateUtil.c.get(Calendar.MONTH), DateUtil.c.get(Calendar.DAY_OF_MONTH));
			}catch(Exception e){
			}
			break;
		}
		return null;
	}
	
	private int getDirPosition(int dirId){
		if(cursor.mDirCursor.moveToFirst()){
			do{
				if(cursor.mDirCursor.getInt(0) == dirId)
					return cursor.mDirCursor.getPosition();
			}while(cursor.mDirCursor.moveToNext());
		}
		return -1;
	}
	
	private void fillForm(){
		//cursor.queryDirections();
		if(cursor.filter.mTaskListPosition!=-1){
			
			cursor.mTaskListCursor.moveToPosition((int)cursor.filter.mTaskListPosition);
			cursor.filter.mTaskId = cursor.mTaskListCursor.getLong(0);
			mName.setText(cursor.mTaskListCursor.getString(1));
			try{
				mBeginDate.setText(DateUtil.getClientDate(cursor.mTaskListCursor.getString(2)));
			}catch(ParseException pe){
			}
			try{
				mActualDate.setText(DateUtil.getClientDate(cursor.mTaskListCursor.getString(3)));
			}catch(ParseException pe){
			}
			try{
				mDeadline.setText(DateUtil.getClientDate(cursor.mTaskListCursor.getString(7)));
			}catch(ParseException pe){
			}
			mStatus.setChecked(cursor.mTaskListCursor.getInt(4)==TaskItem.STATUS_YES);
			mPriority.setSelection(cursor.mTaskListCursor.getInt(5));
			mDirection.setAdapter(cursor.getDirectionAdapter(false));
			mDirection.setSelection(getDirPosition(cursor.mTaskListCursor.getInt(8)));			
		}else{
			mBeginDate.setText(DateUtil.getClientDate(DateUtil.getToday()));
			mActualDate.setText(DateUtil.getClientDate(DateUtil.getToday()));
			mDeadline.setText(DateUtil.getClientDate(DateUtil.getToday()));
			mStatus.setChecked(TaskItem.STATUS_NO==TaskItem.STATUS_YES);
			mPriority.setSelection(TaskItem.PRIORITY_IMPORTANT_FAST);
			mDirection.setAdapter(cursor.getDirectionAdapter(false));
		}
	}
	
	private void save(){
		ContentValues values = new ContentValues(2);
		values.put(DiaryDbHelper.TASK_NAME, mName.getText().toString());
		values.put(DiaryDbHelper.TASK_STATUS, mStatus.isChecked()? TaskItem.STATUS_YES:TaskItem.STATUS_NO);
		try{
			values.put(DiaryDbHelper.TASK_BEGIN_DATE, DateUtil.getDbDate(mBeginDate.getText().toString()));
		}catch(ParseException pe){
		}
		try{
			values.put(DiaryDbHelper.TASK_ACTUAL_DATE, DateUtil.getDbDate(mActualDate.getText().toString()));
		}catch(ParseException pe){
		}
		values.put(DiaryDbHelper.TASK_PRIORITY, mPriority.getSelectedItemPosition());
		values.put(DiaryDbHelper.TASK_USER_ID, TaskItem.USER_ID_VALUE);
		try{
			values.put(DiaryDbHelper.TASK_DEADLINE, DateUtil.getDbDate(mDeadline.getText().toString()));
		}catch(ParseException pe){
		}
		cursor.mDirCursor.moveToPosition(mDirection.getSelectedItemPosition());
		values.put(DiaryDbHelper.TASK_DIRECTION_ID, cursor.mDirCursor.getInt(0));
		if(cursor.filter.mTaskId != -1){
			getContentResolver().update(TaskProvider.CONTENT_URI
										, values
										, "_ID="+cursor.filter.mTaskId, null);
		}else{
			getContentResolver().insert(TaskProvider.CONTENT_URI, values);
		}
	} 
}
