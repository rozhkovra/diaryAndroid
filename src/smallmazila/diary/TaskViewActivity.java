package smallmazila.diary;

import java.text.ParseException;

import smallmazila.diary.animation.FlipActivities;
import smallmazila.diary.framework.CursorFilter;
import smallmazila.diary.framework.DiaryActivity;
import smallmazila.diary.framework.DiaryCursor;
import smallmazila.diary.util.DateUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TaskViewActivity extends DiaryActivity{
	private DiaryCursor cursor;

	TextView mText;
	TextView mDirection;
	TextView mPriority;
	TextView mDeadline;
	TextView mStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_view);
		mFliper = new FlipActivities(this, (LinearLayout)findViewById(R.id.task_view), null);
		mFliper.initialize();
		mText = (TextView)findViewById(R.id.name_view);
		mPriority = (TextView)findViewById(R.id.priority_view);
		mDeadline = (TextView)findViewById(R.id.deadline_view);
		mDirection  = (TextView)findViewById(R.id.direction_view);
		mStatus = (TextView)findViewById(R.id.status_view);
		
		cursor = new DiaryCursor(getApplicationContext(),new CursorFilter());
		Bundle extras = getIntent().getExtras();
		cursor.filter = (CursorFilter)extras.get(CursorFilter.DIARYCURSORFILTER);
		cursor.queryTaskList();
		cursor.queryDirections();

		fillForm();

	}
	
	private void fillForm(){
		setTitle("Task "+(cursor.filter.mTaskListPosition+1)+" from "+cursor.mTaskListCursor.getCount());
		cursor.mTaskListCursor.moveToPosition((int)cursor.filter.mTaskListPosition);
		cursor.filter.mTaskId = cursor.mTaskListCursor.getLong(0);
		mText.setText(cursor.mTaskListCursor.getString(1));
		try{
			mDeadline.setText(DateUtil.getClientDate(cursor.mTaskListCursor.getString(7)));
		}catch(ParseException pe){
		}
		mPriority.setText(TaskItem.getPriorityName(cursor.mTaskListCursor.getInt(5)));
		mDirection.setText(cursor.mTaskListCursor.getString(9));
		mStatus.setText(TaskItem.getStatusName(cursor.mTaskListCursor.getInt(4)));
		Button prevBtn = (Button)findViewById(R.id.prevBtn);
		if(cursor.filter.mTaskListPosition!=-1 && cursor.mTaskListCursor!=null){
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
		if(cursor.filter.mTaskListPosition!=-1 && cursor.mTaskListCursor!=null){
			if(!cursor.mTaskListCursor.isLast()){
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
	}
	
	protected void startTask(){
		Intent intent = new Intent();
		intent.putExtra(CursorFilter.DIARYCURSORFILTER, cursor.filter);
		intent.setClass(this, TaskViewActivity.class);
		intent.setClass(this, TaskActivity.class);
		mFliper.toIntent = intent;
		mFliper.flip();
	}
}
