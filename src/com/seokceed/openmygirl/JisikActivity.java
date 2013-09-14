package com.seokceed.openmygirl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class JisikActivity extends ContentADActivity implements OnClickListener{
	
	private EditText et_name, et_text1, et_text2, et_text3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jisik);
		et_name = (EditText) findViewById(R.id.et_name);
		et_text1 = (EditText) findViewById(R.id.et_text1);
		et_text2 = (EditText) findViewById(R.id.et_text2);
		et_text3 = (EditText) findViewById(R.id.et_text3);
		
//		adView = (MobileAdView) findViewById(R.id.ad_banner);
//		adView.setListener(this);
//		adView.start();
		
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
	}
	
	private String[] checkForm() {
		String name = et_name.getText().toString();
		String text = et_text1.getText().toString();
		String text2 = et_text2.getText().toString();
		String text3 = et_text3.getText().toString();
		
		String[] contents = {name, text, text2, text3};
		return contents;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			String[] result = checkForm();
			if (result != null) {
				Intent i = getIntent();
				i.putExtra(ContentSelectActivity.EXTRA_ARR_TAG, result);
				setResult(RESULT_OK, i);
				finish();
			}
			break;

		case R.id.btn_cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}

}
