package smallmazila.diary;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TaskItem extends HashMap<String,String>{
	private static final long serialVersionUID = 1L;
	public static final String ID = "_ID";
	public static final String NAME = "name";
	public static final String BEGIN_DATE = "begin_date";
	public static final String ACTUAL_DATE = "actual_date";
	public static final String STATUS = "status";
	public static final String PRIORITY = "priority";
	public static final String USER_ID = "user_id";
	public static final String DEADLINE = "deadline";
	public static final String DIRECTION_ID = "direction_id";
	
	public static final String TASK_CURSOR_POSITION = "task_cursor_position";
	
	public static final int STATUS_YES = 1;
	public static final int STATUS_NO = 0;
	
	public static final int PRIORITY_IMPORTANT_FAST = 1;
	public static final int PRIORITY_IMPORTANT_NOFAST = 2;
	public static final int PRIORITY_NOIMPORTANT_FAST = 3;
	public static final int PRIORITY_LONG = 0;
	public static final int PRIORITY_ALL = -1;
	
	public static final String USER_ID_VALUE = "1";
	
	private static Map<Integer, String> statusName = new HashMap<Integer, String>();
	private static Map<Integer, String> priorityName = new LinkedHashMap<Integer, String>();
	
	static{
		statusName.put(STATUS_YES, "Готово");
		statusName.put(STATUS_NO, "В работе");
		
		//priorityName.put(PRIORITY_ALL, "Все");
		priorityName.put(PRIORITY_LONG, "Долгосрочная");
		priorityName.put(PRIORITY_IMPORTANT_FAST, "Важно и срочно");
		priorityName.put(PRIORITY_IMPORTANT_NOFAST, "Важно и не срочно");
	}

	public TaskItem(String name, String begin_date, String actual_date, String status, String priority, String deadline){
		super();
		super.put(NAME, name);
		super.put(BEGIN_DATE, begin_date);
		super.put(ACTUAL_DATE, actual_date);
		super.put(STATUS, status);
		super.put(PRIORITY, priority);
		super.put(DEADLINE, deadline);
	}
	
	public TaskItem(String name){
		super();
		super.put(NAME,name);
	}

	public String getName(){
		return super.get(NAME).toString();
	}
		
	public String getBeginDate(){
		return super.get(BEGIN_DATE).toString();
	}

	public String getActualDate(){
		return super.get(ACTUAL_DATE).toString();
	}

	public String getStatus(){
		return super.get(STATUS).toString();
	}
	
	public String getPriority(){
		return super.get(PRIORITY).toString();
	}
	
	public static String getPriorityName(int priority){
		return priorityName.get(priority);
	}

	public static String getStatusName(int status){
		return statusName.get(status);
	}
	
	public String getUserId(){
		return USER_ID_VALUE;
	}

	public String getDeadline(){
		return super.get(DEADLINE).toString();
	}
	
	public static String[] priorityValues(){
		return priorityName.values().toArray(new String[]{});
	}
}
