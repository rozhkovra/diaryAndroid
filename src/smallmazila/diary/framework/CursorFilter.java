package smallmazila.diary.framework;

import java.io.Serializable;
import java.util.Date;

import smallmazila.diary.util.DateUtil;

public class CursorFilter implements Serializable{
	private static final long serialVersionUID = 2L;
	public static final String DIARYCURSORFILTER = "DiaryCursorFilter";
	
	public int mCurPriority;
	public int mCurDirection;
	public Date mCurDate;
	public long mTaskId;
	public long mTaskListPosition;
	
	public CursorFilter(){
		mCurPriority = -1;
		mCurDate = DateUtil.getToday();
		mCurDirection = -1;
		mTaskId = -1;
		mTaskListPosition=-1;
	}

	public int getMCurPriority(){
		return mCurPriority;
	}
	public int getMCurDirection(){
		return mCurDirection;
	}
	public Date getMCurDate(){
		return mCurDate;
	}
	public long getMTaskId() {
		return mTaskId;
	}

	public void setMTaskId(long mTaskId) {
		this.mTaskId = mTaskId;
	}

	public void setMCurPriority(int mCurPriority) {
		this.mCurPriority = mCurPriority;
	}
	public void setMCurDirection(int mCurDirection) {
		this.mCurDirection = mCurDirection;
	}
	public void seMCurDate(Date mCurDate) {
		this.mCurDate = mCurDate;
	}

	public long getMTaskListPosition() {
		return mTaskListPosition;
	}

	public void setMTaskListPosition(int mTaskListPosition) {
		this.mTaskListPosition = mTaskListPosition;
	}
	
}
