package com.seokceed.openmygirl.contents;

import android.graphics.Bitmap;

public class ResultsContents {
	
	private int bgm_resId;
	private String bgm_info;
	private Bitmap bitmap;
	
	public ResultsContents(int bgm_resId, String bgm_info,Bitmap result) {
		this.bgm_resId = bgm_resId;
		this.bgm_info = bgm_info;
		this.bitmap = result;
	}
	
	public String getBgm_info() {
		return bgm_info;
	}
	public int getBgm_resId() {
		return bgm_resId;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	
}