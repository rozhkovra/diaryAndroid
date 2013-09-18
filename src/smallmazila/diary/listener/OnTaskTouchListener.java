package smallmazila.diary.listener;

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
	TaskTouchListView listView;
	Context context;

	public OnTaskTouchListener(Context context, TaskTouchListView view){
		super();
		this.context = context;
		this.listView = view;
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int pos = listView.pointToPosition((int)event.getX(), (int)event.getY());
		View vv = listView.getChildAt(pos);
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			listView.x=event.getX();
			listView.y=event.getY();
			listView.view=vv;
			downX = event.getX();
			downY = event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			listView.x=event.getX();
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
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		case MotionEvent.ACTION_UP:
			listView.color = Color.YELLOW;
			listView.x=0;
			listView.y=0;
			listView.view=null;
			if(Math.abs(event.getX()-downX) < 10.0 && Math.abs(event.getY()-downY) < 10.0)
				Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
			else if(right(event))
				Toast.makeText(context, "Next day", Toast.LENGTH_SHORT).show();
			else if(left(event))
				Toast.makeText(context, "Prev day", Toast.LENGTH_SHORT).show();
			else if(up(event))
				Toast.makeText(context, "Lower priority", Toast.LENGTH_SHORT).show();
			else if(down(event))
				Toast.makeText(context, "Higher priority", Toast.LENGTH_SHORT).show();
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
}
