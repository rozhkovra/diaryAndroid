package smallmazila.diary;

import java.text.ParseException;
import java.util.Map;

import smallmazila.diary.animation.FlipActivities;
import smallmazila.diary.db.DiaryDbHelper;
import smallmazila.diary.framework.CursorFilter;
import smallmazila.diary.framework.DiaryActivity;
import smallmazila.diary.util.DateUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskViewActivity extends DiaryActivity{
	TextView mText;
	TextView mDirection;
	TextView mPriority;
	TextView mDeadline;
	TextView mStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_view);

		mText = (TextView)findViewById(R.id.name_view);
		mPriority = (TextView)findViewById(R.id.priority_view);
		mDeadline = (TextView)findViewById(R.id.deadline_view);
		mDirection  = (TextView)findViewById(R.id.direction_view);
		mStatus = (TextView)findViewById(R.id.status_view);
		
		Button editBtn = (Button)findViewById(R.id.editBtn);
		editBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startTask();
				if(mClickId != -1){
					mSoundPool.play(mClickId, 1, 1, 0, 0, 1);
				}
			}
		});
		Button toTaskListBtn = (Button)findViewById(R.id.toTaskList);
		toTaskListBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFliper.backPressed();				
				if(mClickId != -1){
					mSoundPool.play(mClickId, 1, 1, 0, 0, 1);
				}
			}
		});
		mFliper.fromLayout = (LinearLayout)findViewById(R.id.task_view);
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
	
	private void fillForm(){
		Map<String, Object> hm = cursor.mTaskList.get((int)cursor.filter.mTaskListPosition); 		
		setTitle("Task "+(cursor.filter.mTaskListPosition+1)+" from "+cursor.mTaskList.size());
		cursor.filter.mTaskId = Long.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_ID).toString());	
		mText.setText(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_NAME).toString());
		try{
			mDeadline.setText(DateUtil.getClientDate(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_DEADLINE).toString()));
		}catch(ParseException pe){
		}
		mPriority.setText(TaskItem.getPriorityName(Integer.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_PRIORITY).toString())));
		mDirection.setText(hm.get(DiaryDbHelper.TASK_DIRECTIONS_DIRECTIONS_NAME).toString());
		mStatus.setText(TaskItem.getStatusName(Integer.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_STATUS).toString())));
		Button prevBtn = (Button)findViewById(R.id.prevBtn);
		if(cursor.filter.mTaskListPosition!=-1){
			if(cursor.filter.mTaskListPosition>0){
				prevBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						cursor.filter.mTaskListPosition = cursor.filter.mTaskListPosition-1;
						fillForm();
						if(mClickId != -1){
							mSoundPool.play(mClickId, 1, 1, 0, 0, 1);
						}
					}
				});
				prevBtn.setEnabled(true);
			}else{
				prevBtn.setEnabled(false);
			}
		}else{
			prevBtn.setEnabled(false);
		}
		
		Button nextBtn = (Button)findViewById(R.id.nextBtn);
		if(cursor.filter.mTaskListPosition!=-1){
			if(cursor.filter.mTaskListPosition!=cursor.mTaskList.size()-1){
				nextBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						cursor.filter.mTaskListPosition = cursor.filter.mTaskListPosition+1; 
						fillForm();
						if(mClickId != -1){
							mSoundPool.play(mClickId, 1, 1, 0, 0, 1);
						}
					}
				});
				nextBtn.setEnabled(true);
			}else{
				nextBtn.setEnabled(false);
			}
		}else{
			nextBtn.setEnabled(false);
		}
		
	}
	
	protected void startTask(){
		Intent intent = new Intent();
		intent.putExtra(CursorFilter.DIARYCURSORFILTER, cursor.filter);
		intent.setClass(this, TaskActivity.class);
		mFliper.toIntent = intent;
		mFliper.flip();
	}
}
