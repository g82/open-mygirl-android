package com.seokceed.openmygirl;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

public class OllehActivity extends ContentADActivity implements OnClickListener {
	
	private EditText et_olleh;
	private CheckBox chk_really;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_olleh);
		findViewById(R.id.btn_ollehok).setOnClickListener(this);
		findViewById(R.id.btn_back).setOnClickListener(this);
		
		et_olleh = (EditText) findViewById(R.id.et_ollehtext);
		chk_really = (CheckBox) findViewById(R.id.chk_olleh_really);
		
//		adView = (MobileAdView) findViewById(R.id.ad_banner);
//		adView.setListener(this);
//		adView.start();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_ollehok:
			String[] result = checkForm();
			if (result != null) {
				Intent i = getIntent();
				i.putExtra(ContentSelectActivity.EXTRA_ARR_TAG, result);
				setResult(RESULT_OK, i);
				finish();
			}
			break;

		case R.id.btn_back:
			setResult(RESULT_CANCELED);
			finish();
			break;
		}
	}

	private String[] checkForm() {
		String text = et_olleh.getText().toString();
		String really = (chk_really.isChecked()) ? chk_really.getText().toString() : null;
		String[] contents = {text, really};
		return contents;
	}

}
