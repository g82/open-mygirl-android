package com.seokceed.openmygirl.dialog;

import android.app.Dialog;
import android.content.Context;

public class MultiTextDialog extends Dialog {
	
	protected OnContentsSelectListener listener;

	public MultiTextDialog(Context context) {
		super(context);
	}
	
	public MultiTextDialog(Context context, OnContentsSelectListener listener){
		super(context);
		this.listener = listener;
		setCanceledOnTouchOutside(false);
	}

}
