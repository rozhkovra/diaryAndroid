package smallmazila.diary.framework;

import java.io.IOException;

import smallmazila.diary.animation.FlipActivities;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

public abstract class DiaryActivity extends Activity {
	protected FlipActivities mFliper = null;
	protected SoundPool mSoundPool = null;
	protected int mClickId = -1;
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
		mFliper = new FlipActivities(this);
		mFliper.initialize();

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
		super.onPause();
	}
}
