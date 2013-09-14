package com.seokceed.openmygirl.share;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import com.kakao.kakaolink.KakaoLink;
import com.seokceed.openmygirl.R;

public class KakaoShare {
	
	//see AndroidMenifest.
	private static final String EXECUTE_URL = "MyGirl://startActivity";
	
	private static final String INSTALL_URL = "market://details?id=com.seokceed.mygirl";
	
	private static final String MARKET_URL = "https://play.google.com/store/apps/details?id=com.seokceed.mygirl";
	
	
	public static void shareImageKakao(Activity activity, Uri imagePath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("image/png");
		intent.putExtra(Intent.EXTRA_STREAM, imagePath);
//		intent.setPackage("com.kakao.talk");
		activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share_what)));
	}
	
	public static void shareAppKakao(Activity activity) {
		ArrayList<Map<String, String>> metaInfoArray = new ArrayList<Map<String, String>>();
		Map<String, String> metaInfoAndroid = new Hashtable<String, String>(1);
		metaInfoAndroid.put("os", "android");
		metaInfoAndroid.put("devicetype", "phone");
		metaInfoAndroid.put("installurl", INSTALL_URL);
		metaInfoAndroid.put("executeurl", EXECUTE_URL);
		
		metaInfoArray.add(metaInfoAndroid);
		
		KakaoLink kakaoLink = KakaoLink.getLink(activity.getApplicationContext());
		if (!kakaoLink.isAvailableIntent()) return;
		try {
			kakaoLink.openKakaoAppLink(activity, MARKET_URL, activity.getString(R.string.kakao_share_msg), 
					activity.getPackageName(), 
					activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName, 
					activity.getString(R.string.app_name), "UTF-8", metaInfoArray);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}		
	}

}
