package com.seokceed.openmygirl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class LOLActivity extends Activity implements OnClickListener {

	private EditText et_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lol);
		et_name = (EditText) findViewById(R.id.et_name);
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
	}

	private String[] checkForm() {
		String name = et_name.getText().toString();
		String[] contents = {name};
		return contents;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			String[] results = checkForm();
			if (results != null) {
				Intent i = getIntent();
				i.putExtra(ContentSelectActivity.EXTRA_ARR_TAG, results);
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
