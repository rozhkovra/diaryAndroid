package smallmazila.diary;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import smallmazila.diary.animation.FlipActivities;
import smallmazila.diary.db.DiaryDbHelper;
import smallmazila.diary.db.TaskProvider;
import smallmazila.diary.framework.CursorFilter;
import smallmazila.diary.framework.DiaryActivity;
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
		mFliper.fromLayout = (LinearLayout)findViewById(R.id.task);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Bundle extras = getIntent().getExtras();
		cursor.filter = (CursorFilter)extras.get(CursorFilter.DIARYCURSORFILTER);
		cursor.queryTaskList();
		cursor.queryDirections();
		fillForm();
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
		for(int i = 0; i < cursor.mDirectionList.size(); i++){
			if(Integer.valueOf(cursor.mDirectionList.get(i).get(DiaryDbHelper.DIRECTIONS_ID).toString()) == dirId)
				return i;			
		}
		return -1;
	}
	
	private void fillForm(){
		if(cursor.filter.mTaskListPosition!=-1){
			Map<String, Object> hm = cursor.mTaskList.get((int)cursor.filter.mTaskListPosition); 
			cursor.filter.mTaskId = Long.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_ID).toString());			
			mName.setText(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_NAME).toString());
			try{
				mBeginDate.setText(DateUtil.getClientDate(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_BEGIN_DATE).toString()));
			}catch(ParseException pe){
			}
			try{
				mActualDate.setText(DateUtil.getClientDate(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_ACTUAL_DATE).toString()));
			}catch(ParseException pe){
			}
			try{
				mDeadline.setText(DateUtil.getClientDate(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_DEADLINE).toString()));
			}catch(ParseException pe){
			}
			mStatus.setChecked(Integer.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_STATUS).toString())==TaskItem.STATUS_YES);
			mPriority.setSelection(Integer.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_PRIORITY).toString()));
			mDirection.setAdapter(cursor.getDirectionSpinnerAdapter(false));
			mDirection.setSelection(getDirPosition(Integer.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_DIRECTION_ID).toString())));			
		}else{
			mBeginDate.setText(DateUtil.getClientDate(DateUtil.getToday()));
			mActualDate.setText(DateUtil.getClientDate(DateUtil.getToday()));
			mDeadline.setText(DateUtil.getClientDate(DateUtil.getToday()));
			mStatus.setChecked(TaskItem.STATUS_NO==TaskItem.STATUS_YES);
			mPriority.setSelection(TaskItem.PRIORITY_IMPORTANT_FAST);
			mDirection.setAdapter(cursor.getDirectionSpinnerAdapter(false));
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
		Map<String, Object> hm = cursor.mDirectionList.get(mDirection.getSelectedItemPosition()); 					
		values.put(DiaryDbHelper.TASK_DIRECTION_ID, Integer.valueOf(hm.get(DiaryDbHelper.DIRECTIONS_ID).toString()));
		if(cursor.filter.mTaskId != -1){
			getContentResolver().update(TaskProvider.CONTENT_URI
										, values
										, "_ID="+cursor.filter.mTaskId, null);
		}else{
			getContentResolver().insert(TaskProvider.CONTENT_URI, values);
		}
	} 
}
