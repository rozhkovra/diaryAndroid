package smallmazila.diary.adapter;

import java.util.List;
import java.util.Map;

import smallmazila.diary.R;
import smallmazila.diary.TaskItem;
import smallmazila.diary.db.DiaryDbHelper;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class TaskListSimpleAdapter extends SimpleAdapter {
	private List<? extends Map<String, Object>> data;
	public TaskListSimpleAdapter(Context context,
			List<? extends Map<String, Object>> data, int layout, String[] from,
			int[] to) {
		super(context, data, layout, from, to);
		this.data = data;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {  
		Map<String, Object> hm = data.get(position); 
		int priority = Integer.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_PRIORITY).toString());
		int status = Integer.valueOf(hm.get(DiaryDbHelper.TASK_DIRECTIONS_TASK_STATUS).toString());
		View view = super.getView(position, convertView, parent);  
		if (priority == TaskItem.PRIORITY_LONG) {
			view.setBackgroundColor(0xfff9ab91);  
		} else {
			view.setBackgroundColor(0xffFFFF78);
/*			if(priority == TaskItem.PRIORITY_IMPORTANT_NOFAST){
				TextView text = (TextView)view.findViewById(R.id.name);
				text.setTextSize(12);
			}
*/		}
		if(status == TaskItem.STATUS_YES)
			view.setBackgroundColor(0xff93F09C);

		return view;  
	}
}
