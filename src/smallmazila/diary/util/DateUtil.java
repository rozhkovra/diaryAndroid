package smallmazila.diary.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static final SimpleDateFormat sdfClient = new SimpleDateFormat("dd.MM.yyyy");
	public static final SimpleDateFormat sdfDb = new SimpleDateFormat("yyyy-MM-dd"); 
	public static Calendar c = Calendar.getInstance();
	public static final String[] daysOfWeek = {"Воскресенье","Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"};
	public static final String[] daysOfWeekShort = {"Вс","Пн", "Вт", "Ср", "Чт", "Пт", "Сб"};
	
	public static String getDate(int year, int month, int day){
		c.set(year, month, day);
		return sdfClient.format(c.getTime());
	}

	public static String getDate(Date date){
		return sdfClient.format(date);
	}
	
	public static Date getDate(String date) throws ParseException{
		return sdfClient.parse(date);
	}
	
	public static Date getYesterday(Date date){
		long millis = date.getTime();
		long yesterdayMillis = millis - 24*60*60*1000;
		return new Date(yesterdayMillis);
	}
	
	public static Date getTomorrow(Date date){
		long millis = date.getTime();
		long yesterdayMillis = millis + 24*60*60*1000;
		return new Date(yesterdayMillis);
	}
	
	public static String getDbDate(String clientDate) throws ParseException{
		return sdfDb.format(sdfClient.parse(clientDate));
	}
	
	public static String getDbDate(Date date){
		return sdfDb.format(date);
	}
	
	public static String getClientDate(Date date){
		return sdfClient.format(date);
	}
	
	public static String getClientDate(String dbDate) throws ParseException{
		return sdfClient.format(sdfDb.parse(dbDate));
	}
	
	public static String getDayOfWeek(Date date){
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_WEEK);
		return daysOfWeek[day-1];
	}
	
	public static String getDayOfWeekShort(Date date){
		c.setTime(date);
		int day = c.get(Calendar.DAY_OF_WEEK);
		return daysOfWeekShort[day-1];
	}
	
	public static Date getToday(){
		return new Date();
	}
}
