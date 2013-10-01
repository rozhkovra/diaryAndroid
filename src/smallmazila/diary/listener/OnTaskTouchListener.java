package smallmazila.diary.listener;

import java.util.Date;

import smallmazila.diary.framework.DiaryCursor;
import smallmazila.diary.view.TaskTouchListView;
import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class OnTaskTouchListener implements OnTouchListener {
	private float downX;
	private float downY;
	public final static int X_PRECISION = 300;
	public final static int Y_PRECISION = 50;
	public final static int LONG_TIME_CLICK_LIMIT = 1000;
	TaskTouchListView listView;
	Context context;
	private Date startTouch;
	private DiaryCursor cursor;

	public OnTaskTouchListener(Context context, TaskTouchListView view, DiaryCursor cursor){
		super();
		this.context = context;
		this.listView = view;
		this.cursor = cursor;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int pos = listView.pointToPosition((int)event.getX(), (int)event.getY());
		View vv = listView.getChildAt(pos);
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			startTouch = new Date();
			downX = event.getX();
			downY = event.getY();			
			break;
		case MotionEvent.ACTION_MOVE:
			if(isLongClick()){
				listView.x=event.getX();
				listView.y=event.getY();
				listView.view=vv;
				if(vv!=null)
					listView.y=vv.getTop();
				if(right(event) || left(event))
					listView.color = Color.GREEN;
				else
					listView.color = Color.YELLOW;
				if(up(event) || down(event)){
					listView.y=event.getY();
					listView.color = Color.RED;
				}
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_UP:
			listView.color = Color.YELLOW;
			listView.x=0;
			listView.y=0;
			listView.view=null;
			if(!isLongClick()){
				// go to view tasks
				Toast.makeText(context, "Short click", Toast.LENGTH_SHORT).show();
				Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(context, "Long click", Toast.LENGTH_SHORT).show();
				if(right(event)){
					// move task to next day
					cursor.taskNextDay(pos);
					Toast.makeText(context, "Next day", Toast.LENGTH_SHORT).show();
				}else if(left(event)){
					// move task to prev day
					cursor.taskPrevDay(pos);
					Toast.makeText(context, "Prev day", Toast.LENGTH_SHORT).show();
				}else if(up(event)){
					// change task priority to lower
					cursor.taskLower(pos);
					Toast.makeText(context, "Lower priority", Toast.LENGTH_SHORT).show();
				}else if(down(event)){
					cursor.taskHigher(pos);
					// change task priority to higher 
					Toast.makeText(context, "Higher priority", Toast.LENGTH_SHORT).show();
				}
				cursor.queryTaskList();
				listView.setAdapter(cursor.getTaskListAdapter());
			}	
			break;
		}
		return true;
	}

	boolean right(MotionEvent event){
		return event.getX() - downX > X_PRECISION;
	}

	boolean left(MotionEvent event){
		return event.getX() - downX < -X_PRECISION;
	}

	boolean down(MotionEvent event){
		return event.getY() - downY < -Y_PRECISION;
	}
	
	boolean up(MotionEvent event){
		return event.getY() - downY > Y_PRECISION;
	}

	boolean isLongClick(){
		return new Date().getTime() - startTouch .getTime() > LONG_TIME_CLICK_LIMIT;
	}
}
