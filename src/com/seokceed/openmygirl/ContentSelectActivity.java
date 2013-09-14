package com.seokceed.openmygirl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.seokceed.openmygirl.contents.ContentsData;

public class ContentSelectActivity extends Activity implements OnItemClickListener{
	
	public static final String EXTRA_ARR_TAG = "EXTRA_ARR_TAG";
	public static final String EXTRA_INT_TAG = "EXTRA_INT_TAG";
	public static final String EXTRA_PATH_TAG = "EXTRA_PATH_TAG";
	
	GridView gv_contents;
	ContentAdapter adapter;
	
	private String mImagePath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent extra = getIntent();
		mImagePath = extra.getStringExtra(EXTRA_PATH_TAG);
		
		if (mImagePath == null) finish(); 
			
		setContentView(R.layout.activity_contentselect);
		
		gv_contents = (GridView) findViewById(R.id.grid_contents);
		gv_contents.setOnItemClickListener(this);
		
		//load contents list.
		adapter = new ContentAdapter(this, ContentsData.arrContents);
		
		gv_contents.setAdapter(adapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			data.putExtra(EXTRA_INT_TAG, requestCode);
			setResult(RESULT_OK, data);
			finish();
		}
	}
	
// request code is position
	
	@Override
	public void onItemClick(AdapterView<?> gridview, View v, int position, long id) {
		Intent i;
		switch (position) {
		case 0:
			//bene
			i = new Intent();
			i.putExtra(ContentSelectActivity.EXTRA_PATH_TAG, mImagePath);
			i.putExtra(ContentSelectActivity.EXTRA_INT_TAG, position);
			setResult(RESULT_OK, i);
			finish();
			break;

		case 1:
			//human theater
			i = new Intent(ContentSelectActivity.this, HumanTheaterActivity.class);
			i.putExtra(EXTRA_PATH_TAG, mImagePath);
			startActivityForResult(i, position);
			break;
			
		case 2:
			//call jisik
			i = new Intent(ContentSelectActivity.this, JisikActivity.class);
			i.putExtra(EXTRA_PATH_TAG, mImagePath);
			startActivityForResult(i, position);
			break;
			
		case 3:
			//call sing
			i = new Intent(ContentSelectActivity.this, SingroomActivity.class);
			i.putExtra(EXTRA_PATH_TAG, mImagePath);
			startActivityForResult(i, position);
			break;
			
		case 4:
			i = new Intent(ContentSelectActivity.this, WooparooActivity.class);
			i.putExtra(EXTRA_PATH_TAG, mImagePath);
			startActivityForResult(i, position);
			break;
			
		case 5:
			 i = new Intent(ContentSelectActivity.this, OllehActivity.class);
			 i.putExtra(EXTRA_PATH_TAG, mImagePath);
			 startActivityForResult(i, position);
			break;
		}
	}

}
