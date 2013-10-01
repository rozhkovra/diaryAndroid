package smallmazila.diary.animation;

import android.app.Activity;
import android.content.Intent;
import android.view.Display;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.LinearLayout;

public class FlipActivities {
	public Activity fromActivity;
	public LinearLayout fromLayout;
	public Intent toIntent;
	
	public enum ACTIVITYSTATUS {NONE, PAUSED, BACKPRESSED}
	public ACTIVITYSTATUS activityStatus;
	
	public FlipActivities(Activity fromActivity){
		this.fromActivity = fromActivity;
	}
	
	public void initialize(){
		fromActivity.overridePendingTransition(0, 0);
		activityStatus = ACTIVITYSTATUS.NONE;
	}
	
	public void flip(){
		Animation anim = applyRotation(0, 90);
		anim.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				fromActivity.startActivity(toIntent);
			}
		});
		fromLayout.startAnimation(anim);
	}

	public void flipForResult(){
		Animation anim = applyRotation(0, 90);
		anim.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				fromActivity.startActivityForResult(toIntent, toIntent.getIntExtra("actionId", 0));
			}
		});
		fromLayout.startAnimation(anim);
	}
	
	public void resume(){
		fromLayout.startAnimation(applyRotation(-90, 0));		
	}
	
	public void backPressed(){
		Animation anim = applyRotation(0, 90);
		anim.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				activityStatus = ACTIVITYSTATUS.BACKPRESSED;
				fromActivity.onBackPressed();
			}
		});
		fromLayout.startAnimation(anim);		
	}
	
	public void finish(){
		Animation anim = applyRotation(0, 90);
		anim.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				fromActivity.finish();
			}
		});
		fromLayout.startAnimation(anim);		
	}

	public boolean isBackPressed(){
		return activityStatus == ACTIVITYSTATUS.BACKPRESSED;
	}
	
	private Animation applyRotation(float start, float end){
		Display display = ((WindowManager) fromActivity.getSystemService(Activity.WINDOW_SERVICE)).getDefaultDisplay();		
		final float centerX = display.getWidth() / 2.0f;
		final float centerY = display.getHeight() / 2.0f;

		final Flip3dAnimation rotation = new Flip3dAnimation(start, end, centerX, centerY);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		
		return rotation;
	}

}
