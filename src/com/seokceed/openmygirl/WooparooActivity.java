package com.seokceed.openmygirl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.seokceed.openmygirl.R;

public class WooparooActivity extends ContentADActivity implements OnClickListener {
	
	private EditText et_name, et_age, et_loca, et_like, et_text, et_secretion;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wooparoo);
		et_name = (EditText) findViewById(R.id.et_name);
		et_age = (EditText) findViewById(R.id.et_age);
		et_loca = (EditText) findViewById(R.id.et_location);
		et_like = (EditText) findViewById(R.id.et_like);
		et_text = (EditText) findViewById(R.id.et_text);
		et_secretion = (EditText) findViewById(R.id.et_secretion);
		
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		
//		adView = (MobileAdView) findViewById(R.id.ad_banner);
//		adView.setListener(this);
//		adView.start();
	}
	
	private String[] checkForm() {
		String name = et_name.getText().toString();
		String age = et_age.getText().toString();
		String loca = et_loca.getText().toString();
		String like = et_like.getText().toString();
		String text = et_text.getText().toString();
		String secretion = et_secretion.getText().toString();
		
		String[] contents = {name, age, loca, like, text, secretion};
		return contents;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			String[] result = checkForm();
			Intent i = getIntent();
			i.putExtra(ContentSelectActivity.EXTRA_ARR_TAG, result);
			setResult(RESULT_OK, i);
			finish();
			break;
			
		case R.id.btn_cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}

}
