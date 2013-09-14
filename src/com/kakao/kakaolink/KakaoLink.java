package com.kakao.kakaolink;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

/**
 * Copyright 2011 Kakao Corp. All rights reserved.
 * 
 * @author kakaolink@kakao.com
 * @version 1.0
 * 
 */
public class KakaoLink {

	private static KakaoLink kakaoLink = null;

	private static String KakaoLinkApiVersion = "2.0";
	private static String KakaoLinkURLBaseString = "kakaolink://sendurl";

	private static Charset KakaoLinkCharset = Charset.forName("UTF-8");
	private static String KakaoLinkEncoding = KakaoLinkCharset.name();

	private Context context;
	private String params;

	private KakaoLink(Context context) {
		super();
		this.context = context;
		this.params = getBaseKakaoLinkUrl();
	}

	/**
	 * Return the default singleton instance
	 * 
	 * @param context
	 * @return KakaoLink instance.
	 */
	public static KakaoLink getLink(Context context) {
		if (kakaoLink != null)
			return kakaoLink;

		return new KakaoLink(context);
	}

	/**
	 * Opens kakaoLink for parameter.
	 * 
	 * @param activity
	 * @param params
	 */
	private void openKakaoLink(Activity activity, String params) {
		Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(params));
		activity.startActivity(intent);
	}

	/**
	 * Opens kakaoLink URL for parameters.
	 * 
	 * @param activity
	 * @param url
	 * @param message
	 * @param appId
	 *            your application ID
	 * @param appVer
	 *            your application version
	 * @param appName
	 *            your application name
	 * @param encoding
	 *            recommend UTF-8
	 */
	public void openKakaoLink(Activity activity, String url, String message, String appId, String appVer, String appName, String encoding) {

		if (isEmptyString(url) || isEmptyString(message) || isEmptyString(appId) || isEmptyString(appVer) || isEmptyString(appName) || isEmptyString(encoding))
			throw new IllegalArgumentException();

		try {
			if (KakaoLinkCharset.equals(Charset.forName(encoding)))
				message = new String(message.getBytes(encoding), KakaoLinkEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		this.params = getBaseKakaoLinkUrl();

		appendParam("url", url);
		appendParam("msg", message);
		appendParam("apiver", KakaoLinkApiVersion);
		appendParam("appid", appId);
		appendParam("appver", appVer);
		appendParam("appname", appName);
		appendParam("type", "link");

		openKakaoLink(activity, params);
	}

	/**
	 * Opens kakaoAppLink with parameters.
	 * 
	 * @param activity
	 * @param url
	 * @param message
	 * @param appId
	 *            your application ID
	 * @param appVer
	 *            your application version
	 * @param appName
	 *            your application name
	 * @param encoding
	 *            recommend UTF-8
	 * @param metaInfoArray
	 */
	public void openKakaoAppLink(Activity activity, String url, String message, String appId, String appVer, String appName, String encoding,
			ArrayList<Map<String, String>> metaInfoArray) {

		if (isEmptyString(url) || isEmptyString(message) || isEmptyString(appId) || isEmptyString(appVer) || isEmptyString(appName) || isEmptyString(encoding)) {
			throw new IllegalArgumentException();
		}

		try {
			if (KakaoLinkCharset.equals(Charset.forName(encoding)))
				message = new String(message.getBytes(encoding), KakaoLinkEncoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		this.params = getBaseKakaoLinkUrl();

		appendParam("url", url);
		appendParam("msg", message);
		appendParam("apiver", KakaoLinkApiVersion);
		appendParam("appid", appId);
		appendParam("appver", appVer);
		appendParam("appname", appName);
		appendParam("type", "app");
		appendMetaInfo(metaInfoArray);

		openKakaoLink(activity, params);
	}

	/**
	 * @return Whether the application can open kakaolink URLs.
	 */
	public boolean isAvailableIntent() {
		Uri kakaoLinkTestUri = Uri.parse(KakaoLinkURLBaseString);
		Intent intent = new Intent(Intent.ACTION_SEND, kakaoLinkTestUri);
		List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		if (list == null)
			return false;
		return list.size() > 0;
	}

	private boolean isEmptyString(String str) {
		return (str == null || str.trim().length() == 0);
	}

	private void appendParam(final String name, final String value) {
		try {
			String encodedValue = URLEncoder.encode(value, KakaoLinkEncoding);
			params = params + name + "=" + encodedValue + "&";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void appendMetaInfo(ArrayList<Map<String, String>> metaInfoArray) {
		params += "metainfo=";

		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();

		try {
			for (Map<String, String> metaInfo : metaInfoArray) {
				JSONObject metaObj = new JSONObject();
				for (String key : metaInfo.keySet()) {
					metaObj.put(key, metaInfo.get(key));
				}
				arr.put(metaObj);
			}
			obj.put("metainfo", arr);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		try {
			String encodedValue = URLEncoder.encode(obj.toString(), KakaoLinkEncoding);
			params += encodedValue;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private String getBaseKakaoLinkUrl() {
		return KakaoLinkURLBaseString + "?";
	}
}
