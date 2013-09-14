package com.seokceed.openmygirl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class PokemonActivity extends Activity implements OnClickListener{
	
	private EditText et_name, et_lv, et_skill;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poke);
		et_name = (EditText) findViewById(R.id.et_name);
		et_lv = (EditText) findViewById(R.id.et_lv);
		et_skill = (EditText) findViewById(R.id.et_skill);
		
		findViewById(R.id.btn_ok).setOnClickListener(this);
		findViewById(R.id.btn_cancel).setOnClickListener(this);
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
	
	protected String[] checkForm() {
		String name = et_name.getText().toString();
		String lv = et_lv.getText().toString();
		String nick = et_skill.getText().toString();
		
		String contents[] = {name, lv, nick};
		return contents;
	}

}
