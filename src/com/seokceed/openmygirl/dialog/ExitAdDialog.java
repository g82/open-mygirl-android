package com.seokceed.openmygirl.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.seokceed.openmygirl.R;

public class ExitAdDialog extends MultiTextDialog implements android.view.View.OnClickListener {
	
	private Activity activity;

	public ExitAdDialog(Context context, OnContentsSelectListener listener) {
		super(context, listener);
		activity = (Activity) context;
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_exit);
		findViewById(R.id.btn_exit).setOnClickListener(this);
		findViewById(R.id.iv_adimage).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_exit:
			dismiss();
			activity.finish();
			break;
			
		case R.id.iv_adimage:
			listener.onContentSelect(null);
			break;
		}
		
	}

}
