package com.seokceed.openmygirl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class SingroomActivity extends ContentADActivity implements OnClickListener {
	
	private EditText et_num, et_title, et_singer, et_first, et_second;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sing);
		et_num = (EditText) findViewById(R.id.et_sing_num);
		et_title = (EditText) findViewById(R.id.et_sing_title);
		et_singer = (EditText) findViewById(R.id.et_sing_singer);
		et_first = (EditText) findViewById(R.id.et_first_part);
		et_second = (EditText) findViewById(R.id.et_second_part);
		
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		
//		adView = (MobileAdView) findViewById(R.id.ad_banner);
//		adView.setListener(this);
//		adView.start();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			Intent i = getIntent();
			i.putExtra(ContentSelectActivity.EXTRA_ARR_TAG, checkForm());
			setResult(RESULT_OK, i);
			finish();
			break;

		case R.id.btn_cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}
	
	private String[] checkForm() {
		String num = et_num.getText().toString();
		String title = et_title.getText().toString();
		String singer = et_singer.getText().toString();
		String first = et_first.getText().toString();
		String second = et_second.getText().toString();
		
		String contents[] = {num, title, singer, first, second};
		return contents;
	}

}
