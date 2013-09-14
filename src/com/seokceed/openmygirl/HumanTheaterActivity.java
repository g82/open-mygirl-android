package com.seokceed.openmygirl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.seokceed.openmygirl.R;

public class HumanTheaterActivity extends ContentADActivity implements OnClickListener {
	
	private EditText et_name, et_age, et_job, et_text, et_pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_human);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
		
//		adView = (MobileAdView) findViewById(R.id.ad_banner);
//		adView.setListener(this);
//		adView.start();
		
		et_name = (EditText) findViewById(R.id.et_name);
		et_age = (EditText) findViewById(R.id.et_age);
		et_job = (EditText) findViewById(R.id.et_job);
		et_text = (EditText) findViewById(R.id.et_text);
		et_pd = (EditText) findViewById(R.id.et_pdtext);
		
	}
	
	private String[] checkForm() {
		String name = et_name.getText().toString();
		String age = et_age.getText().toString();
		String job = et_job.getText().toString();
		String text = et_text.getText().toString();
		String pd = et_pd.getText().toString();
		
		String[] contents = {name, age, job, text, pd};
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
