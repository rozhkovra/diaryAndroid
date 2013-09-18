package smallmazila.diary;

import smallmazila.diary.animation.FlipActivities;
import smallmazila.diary.db.DiaryDbHelper;
import smallmazila.diary.db.DirectionProvider;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DirectionsActivity extends Activity {
	private static final int IDM_ADD = 102;
	
	private ListAdapter mAdapter;
	private Cursor mDirCursor;
	private static final String[] mDirContent = new String[]{DiaryDbHelper._ID
		, DiaryDbHelper.DIRECTIONS_NAME
		, DiaryDbHelper.DIRECTIONS_STATUS
		, DiaryDbHelper.DIRECTIONS_USER_ID};
	private FlipActivities mFliper;
	private ListView mList;
	private int mDirId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.directions);
		mList = (ListView)findViewById(R.id.list);
		mFliper = new FlipActivities(this, (LinearLayout)findViewById(R.id.layout), null);
		mFliper.initialize();
		mDirCursor = managedQuery(DirectionProvider.CONTENT_URI, mDirContent, null, null, null);
		mAdapter = new SimpleCursorAdapter(this
				, R.layout.direction_item
				, mDirCursor
				, new String[]{DiaryDbHelper.DIRECTIONS_NAME}
				, new int[]{R.id.name}
				);
		mList.setAdapter(mAdapter);
		mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	mDirCursor.moveToPosition(position);
            	mDirId = mDirCursor.getInt(0);
            	Toast.makeText(getApplicationContext(),"Hello!"+mDirId, Toast.LENGTH_SHORT).show();
            }
		});
		mList.setItemsCanFocus(true);
		
		Button btnAdd = (Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText edit = (EditText)findViewById(R.id.direction);
				String text = edit.getText().toString();
				ContentValues values = new ContentValues(2);
				values.put(DiaryDbHelper.DIRECTIONS_NAME, text);
				values.put(DiaryDbHelper.DIRECTIONS_STATUS, 1);
				values.put(DiaryDbHelper.DIRECTIONS_USER_ID, 1);
				getContentResolver().insert(DirectionProvider.CONTENT_URI
						, values);			
				mDirCursor.requery();
			}
		});
		
		Button btnBack = (Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){	
		switch(item.getItemId()){
			case IDM_ADD: {
				}
				break;
		}
		return (super.onOptionsItemSelected(item));
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
	

}
