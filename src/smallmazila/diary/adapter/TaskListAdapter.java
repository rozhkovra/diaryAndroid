package smallmazila.diary.adapter;

import smallmazila.diary.TaskItem;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class TaskListAdapter extends SimpleCursorAdapter {
	private Cursor mCursor;
	public TaskListAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		mCursor = c;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {  
		mCursor.moveToPosition(position);
		int priority = mCursor.getInt(5);
		int status = mCursor.getInt(4);
		View view = super.getView(position, convertView, parent);  
		if (priority == TaskItem.PRIORITY_LONG) {
			view.setBackgroundColor(0xfff9ab91);  
		} else {
			view.setBackgroundColor(0xffFFFF78);
		}
		if(status == TaskItem.STATUS_YES)
			view.setBackgroundColor(0xff93F09C);

		return view;  
	}
}
