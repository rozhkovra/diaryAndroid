package smallmazila.diary;

import smallmazila.diary.db.DiaryDbHelper;
import smallmazila.diary.db.DirectionProvider;
import smallmazila.diary.framework.DiaryActivity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class DirectionsActivity extends DiaryActivity {
	private ListView mList;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.directions);
		mList = (ListView)findViewById(R.id.list);
		cursor.queryDirections();
		mList.setAdapter(cursor.getDirectionListAdapter());
		mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id){
				Toast.makeText(getApplicationContext(), "Hello!", Toast.LENGTH_SHORT).show();
				if(mClickId != -1){
					mSoundPool.play(mClickId, 1, 1, 0, 0, 1);
				}
			}
		});
		
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
				cursor.queryDirections();
				mList.setAdapter(cursor.getDirectionListAdapter());
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
		mFliper.fromLayout = (LinearLayout)findViewById(R.id.layout);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		cursor.queryDirections();
		mList.setAdapter(cursor.getDirectionListAdapter());
	}
}
