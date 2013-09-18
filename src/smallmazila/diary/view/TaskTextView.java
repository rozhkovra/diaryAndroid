package smallmazila.diary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.widget.TextView;

public class TaskTextView extends TextView {
	Paint paint;
	int x = -1;
	int y = -1;
	
	public TaskTextView(Context context){
		super(context);
		paint = new Paint();
	}
	
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		if(x!=-1 && y!=-1){
			paint.setColor(Color.YELLOW);
			paint.setStyle(Style.FILL);
			canvas.drawRect(new Rect(x,y,x+100,y+100), paint);
			paint.setColor(Color.BLACK);
			paint.setStyle(Style.STROKE);
			canvas.drawRect(new Rect(x,y,x+100,y+100), paint);
			invalidate();
		}
	}
}
