package smallmazila.diary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class TaskTouchListView extends ListView {
	public float x = 0;
	public float y = 0;
	public View view;
	Paint paint;
	public int color = Color.YELLOW;
	public TaskTouchListView(Context context){
		super(context);
		paint = new Paint();
	}
	
	public TaskTouchListView(Context context, AttributeSet attrs){
	    super(context, attrs);
		paint = new Paint();
	}
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		if(view!=null){
			paint.setColor(Color.BLACK);
			paint.setStyle(Style.STROKE);
			canvas.drawRect(x, y, x+view.getWidth(),y+view.getHeight(), paint);
			paint.setColor(color);
			paint.setStyle(Style.FILL);
			canvas.drawRect(x, y, x+view.getWidth(),y+view.getHeight(), paint);
		}
		invalidate();	
	}
}
