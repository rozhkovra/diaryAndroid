package smallmazila.diary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DiaryDbHelper extends SQLiteOpenHelper
	implements BaseColumns{
	public static final int DB_VERSION = 18;

	public static final String DB_DIARY = "smallmazila_diary.db";
	public static final String TABLE_TASK = "task";
	public static final String TASK_NAME = "name";
	public static final String TASK_BEGIN_DATE = "begin_date";
	public static final String TASK_ACTUAL_DATE = "actual_date";
	public static final String TASK_STATUS = "status";
	public static final String TASK_PRIORITY = "priority";
	public static final String TASK_USER_ID = "user_id";
	public static final String TASK_DEADLINE = "deadline";
	public static final String TASK_DIRECTION_ID = "direction_id";
	
	public static final String TABLE_DIRECTIONS = "directions";
	public static final String DIRECTIONS_ID = "_id";
	public static final String DIRECTIONS_NAME = "name";
	public static final String DIRECTIONS_STATUS = "status";
	public static final String DIRECTIONS_USER_ID = "user_id";
//	public static final String DIRECTIONS_PARENT_ID = "parent_id";
	
	public static final String VIEW_TASK_DIRECTION = "task_directions";
	public static final String TASK_DIRECTIONS_TASK_ID = "_id";
	public static final String TASK_DIRECTIONS_TASK_NAME = "task_name";
	public static final String TASK_DIRECTIONS_TASK_BEGIN_DATE = "task_begin_date"; 
	public static final String TASK_DIRECTIONS_TASK_ACTUAL_DATE = "task_actual_date";
	public static final String TASK_DIRECTIONS_TASK_STATUS = "task_status";
	public static final String TASK_DIRECTIONS_TASK_PRIORITY = "task_priority";
	public static final String TASK_DIRECTIONS_TASK_USER_ID = "task_user_id";
	public static final String TASK_DIRECTIONS_TASK_DEADLINE = "task_deadline";
	public static final String TASK_DIRECTIONS_TASK_DIRECTION_ID = "task_direction_id";
	public static final String TASK_DIRECTIONS_DIRECTIONS_NAME = "direction_name";
	public static final String TASK_DIRECTIONS_DIRECTIONS_STATUS = "direction_status";
	public static final String TASK_DIRECTIONS_DIRECTIONS_USER_ID = "direction_user_id";
	
	public DiaryDbHelper(Context context){
		super(context, DB_DIARY, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL("CREATE TABLE "+ TABLE_TASK + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				TASK_NAME + " varchar(255), "+
				TASK_BEGIN_DATE +" VARCHAR(255),"+
				TASK_ACTUAL_DATE +" VARCHAR(255)," +
				TASK_STATUS + " INTEGER," +
				TASK_PRIORITY + " INTEGER," +
				TASK_USER_ID + " INTEGER," +
				TASK_DEADLINE + " VARCHAR(255)," +
				TASK_DIRECTION_ID + " INTEGER);");		

		db.execSQL("CREATE TABLE "+ TABLE_DIRECTIONS + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				DIRECTIONS_NAME + " varchar(255), "+
				DIRECTIONS_STATUS + " INTEGER," +
				TASK_USER_ID + " INTEGER);");
		
		db.execSQL("CREATE VIEW "+VIEW_TASK_DIRECTION+" as "
					+ " SELECT t._ID as " +TASK_DIRECTIONS_TASK_ID+", "
					+ " t."+TASK_NAME+" as "+TASK_DIRECTIONS_TASK_NAME+", "
					+ " t."+TASK_BEGIN_DATE+" as "+TASK_DIRECTIONS_TASK_BEGIN_DATE+", "
					+ " t."+TASK_ACTUAL_DATE+" as "+TASK_DIRECTIONS_TASK_ACTUAL_DATE+", "
					+ " t."+TASK_STATUS+" as "+TASK_DIRECTIONS_TASK_STATUS+", "
					+ " t."+TASK_PRIORITY+" as "+TASK_DIRECTIONS_TASK_PRIORITY+", "
					+ " t."+TASK_USER_ID+" as "+TASK_DIRECTIONS_TASK_USER_ID+", "
					+ " t."+TASK_DEADLINE+" as "+TASK_DIRECTIONS_TASK_DEADLINE+", "
					+ " t."+TASK_DIRECTION_ID+" as "+TASK_DIRECTIONS_TASK_DIRECTION_ID+", "
					+ " d."+DIRECTIONS_NAME+" as "+TASK_DIRECTIONS_DIRECTIONS_NAME+", "
					+ " d."+DIRECTIONS_STATUS+" as "+TASK_DIRECTIONS_DIRECTIONS_STATUS+", "
					+ " d."+DIRECTIONS_USER_ID+" as "+TASK_DIRECTIONS_DIRECTIONS_USER_ID
					+" FROM "+TABLE_TASK+" t, "+TABLE_DIRECTIONS+" d"
					+" WHERE t."+TASK_DIRECTION_ID+"=d._ID"
					);

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		if(oldVersion==14)
			createBackup14(db);
		if(oldVersion==15)
			createBackup15(db);
		if(oldVersion==16)
			createBackup15(db);
		if(oldVersion==17)
			createBackup15(db);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIRECTIONS);
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
			db.execSQL("DROP VIEW IF EXISTS " + VIEW_TASK_DIRECTION);
			onCreate(db);
		if(oldVersion==14 && newVersion==15)
			restoreBackup14_15(db);	
		if(oldVersion==15 && newVersion==16)
			restoreBackup15_16(db);
		if(oldVersion==16 && newVersion==17)
			restoreBackup15_16(db);	
		if(oldVersion==17 && newVersion==18)
			restoreBackup15_16(db);	
	}
	
	private void createBackup14(SQLiteDatabase db){
		db.execSQL("CREATE TABLE "+ TABLE_TASK + "_backup(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				TASK_NAME + " varchar(255), "+
				TASK_BEGIN_DATE +" VARCHAR(255),"+
				TASK_ACTUAL_DATE +" VARCHAR(255)," +
				TASK_STATUS + " INTEGER," +
				TASK_PRIORITY + " INTEGER," +
				TASK_USER_ID + " INTEGER," +
				TASK_DEADLINE + " VARCHAR(255)," +
				TASK_DIRECTION_ID + " INTEGER);");
		
		db.execSQL("INSERT INTO "+TABLE_TASK+"_backup SELECT * FROM "+TABLE_TASK);
	}
	
	private void createBackup15(SQLiteDatabase db){
		db.execSQL("CREATE TABLE "+ TABLE_TASK + "_backup(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				TASK_NAME + " varchar(255), "+
				TASK_BEGIN_DATE +" VARCHAR(255),"+
				TASK_ACTUAL_DATE +" VARCHAR(255)," +
				TASK_STATUS + " INTEGER," +
				TASK_PRIORITY + " INTEGER," +
				TASK_USER_ID + " INTEGER," +
				TASK_DEADLINE + " VARCHAR(255)," +
				TASK_DIRECTION_ID + " INTEGER);");
		
		db.execSQL("INSERT INTO "+TABLE_TASK+"_backup SELECT * FROM "+TABLE_TASK);

		db.execSQL("CREATE TABLE "+ TABLE_DIRECTIONS + "_backup(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				DIRECTIONS_NAME + " varchar(255), "+
				DIRECTIONS_STATUS + " INTEGER," +
				TASK_USER_ID + " INTEGER);");
		
		db.execSQL("INSERT INTO "+TABLE_DIRECTIONS+"_backup SELECT * FROM "+TABLE_DIRECTIONS);
	}
	
	private void restoreBackup14_15(SQLiteDatabase db){		
		db.execSQL("INSERT INTO "+TABLE_TASK+" SELECT * FROM "+TABLE_TASK+"_backup");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK+"_backup");
	}

	private void restoreBackup15_16(SQLiteDatabase db){		
		db.execSQL("INSERT INTO "+TABLE_TASK+" SELECT * FROM "+TABLE_TASK+"_backup");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK+"_backup");
		db.execSQL("INSERT INTO "+TABLE_DIRECTIONS+" SELECT * FROM "+TABLE_DIRECTIONS+"_backup");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIRECTIONS+"_backup");
	}
}
