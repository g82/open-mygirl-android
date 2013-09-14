package com.seokceed.openmygirl;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.seokceed.openmygirl.share.KakaoShare;

public class InfoActivity extends Activity implements OnClickListener{
	
	private static final String MAIL_ADDR = "kkumygirl@gmail.com";
	private static final String PAGE_URL = "http://m.facebook.com/pages/%EA%BE%B8%EC%A5%AC%EC%9B%8C%EB%A7%88%EC%9D%B4%EA%B1%B8/483136381735838?ref=hl";
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		findViewById(R.id.btn_mail).setOnClickListener(this);
		findViewById(R.id.btn_kakao).setOnClickListener(this);
		findViewById(R.id.btn_fb).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_mail:
			Uri uri = Uri.parse("mailto:" + MAIL_ADDR);
			Intent i = new Intent(Intent.ACTION_SENDTO, uri);
			i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.send_mail_title));
			startActivity(i);
			break;

		case R.id.btn_kakao:
			KakaoShare.shareAppKakao(InfoActivity.this);
			break;
			
		case R.id.btn_fb:
			Uri uri2 = Uri.parse(PAGE_URL);
			Intent i2 = new Intent(Intent.ACTION_VIEW, uri2);
			startActivity(i2);
			break;
		}
		
	}

	

}
