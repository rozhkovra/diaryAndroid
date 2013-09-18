package smallmazila.diary.framework;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import smallmazila.diary.R;
import smallmazila.diary.TaskActivity;
import smallmazila.diary.TaskItem;
import smallmazila.diary.adapter.TaskListAdapter;
import smallmazila.diary.animation.FlipActivities;
import smallmazila.diary.db.DiaryDbHelper;
import smallmazila.diary.db.DirectionProvider;
import smallmazila.diary.util.DateUtil;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public abstract class DiaryActivity extends Activity {
	protected FlipActivities mFliper = null;
	protected SoundPool mSoundPool = null;
	protected int mClickId = -1;
	protected static final int IDM_EDIT = 101;
	protected long mId = -1;
	protected long mTaskId = -1;
	protected DiaryCursor cursor = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		mSoundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		try{
			AssetManager assetManager = getAssets();
			AssetFileDescriptor descriptor = assetManager.openFd("click.ogg");
			mClickId = mSoundPool.load(descriptor, 1);
		} catch (IOException e){
		}
		cursor = new DiaryCursor(getApplicationContext(),new CursorFilter());
	}
	
	@Override
	public void onBackPressed(){
		if(mFliper.isBackPressed())
			super.onBackPressed();
		else
			mFliper.backPressed();
	}
	
	@Override
	public void onResume(){
		mFliper.resume();
		super.onResume();
	}
	
	@Override
	public void onPause(){
		cursor.close();
		super.onPause();
	}
}
