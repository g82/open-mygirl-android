/**
   Copyright 2013 Blackhole Studio
   
   first written by Jaehun Seok 03.15.2013
   
   soks@blackholestudio.co.kr
 */

package com.seokceed.openmygirl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.seokceed.openmygirl.contents.ContentsData;
import com.seokceed.openmygirl.contents.RequestContents;
import com.seokceed.openmygirl.contents.ResultsContents;
import com.seokceed.openmygirl.dialog.ExitAdDialog;
import com.seokceed.openmygirl.dialog.OnContentsSelectListener;
import com.seokceed.openmygirl.image.ImageModify;
import com.seokceed.openmygirl.image.MediaFileUtil;
import com.seokceed.openmygirl.share.KakaoShare;

public class MainImageActivity extends Activity implements OnClickListener {

	private static final int REQ_GALLERY = 1000;
	private static final int REQ_CAMERA = 1001;
	private static final int REQ_CONTENTS = 1002;

	private ImageView iv_image;
	private Bitmap convertedBitmap;
	private MediaScannerConnection msc;
	ProgressDialog dialog;
	ViewPager pager_main;
	private RelativeLayout rl_howto;
	
	private Uri cameraUri;
	private Uri savedUri;
	
	private MediaPlayer mediaPlayer;
	
	private ImageModify imageModify;
	
	private boolean isNeedShare = false;
	
	private ExitAdDialog exitAdDialog;
	
//	private MobileAdView adView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pager_main = (ViewPager) findViewById(R.id.pager_main);
		RelativeLayout page_create = (RelativeLayout) getLayoutInflater().inflate(R.layout.page_createimage, null);
		LinearLayout page_whatnext = (LinearLayout) getLayoutInflater().inflate(R.layout.page_whatnext, null);
		
		MainPagerAdapter pagerAdapter = new MainPagerAdapter(page_create, page_whatnext);
		pager_main.setAdapter(pagerAdapter);	

		page_create.findViewById(R.id.btn_save).setOnClickListener(this);
		page_create.findViewById(R.id.btn_gallery).setOnClickListener(this);
		page_create.findViewById(R.id.btn_camera).setOnClickListener(this);
		page_create.findViewById(R.id.btn_share).setOnClickListener(this);
		page_create.findViewById(R.id.rl_info).setOnClickListener(this);
		
		iv_image = (ImageView) page_create.findViewById(R.id.iv_image);
		iv_image.setOnClickListener(this);
		iv_image.setClickable(false);
		
		rl_howto = (RelativeLayout) page_create.findViewById(R.id.rl_howto);
		
//		adView = (MobileAdView) findViewById(R.id.ad_banner);
//		adView.setListener(this);
//		adView.start();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		EasyTracker.getInstance().activityStart(this);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
//		EasyTracker.getInstance().activityStop(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//phone call... etc... bgm stop
		if (mediaPlayer != null && mediaPlayer.isPlaying()){
			mediaPlayer.pause();
		}
	}
	
	@Override
	public void onBackPressed() {
		
		if (convertedBitmap == null) {
			//exit
			
			if (exitAdDialog == null) {
				exitAdDialog = new ExitAdDialog(this, new OnContentsSelectListener() {
					
					@Override
					public void onContentSelect(String[] results) {
						/*
						 * if exit popup ad exist.
						 * 
						 */
						
						
//						EasyTracker.getTracker().sendEvent("user_choice", "click_ad", "finish_ad", 0l);
//						Toast.makeText(MainImageActivity.this, getString(R.string.clicked_ad), Toast.LENGTH_SHORT).show();
//						
//						Intent i = new Intent(Intent.ACTION_VIEW);
//						i.addCategory(Intent.CATEGORY_DEFAULT);
//						i.setData(Uri.parse("market://details?id=com.wow.stylecash"));
//						startActivity(i);
					}
				});
			}
			exitAdDialog.show();
		}
		else {
			//remove bitmap state clear music stop
			//howto view visible
			iv_image.setVisibility(View.GONE);
			iv_image.setImageBitmap(null);
			convertedBitmap.recycle();
			convertedBitmap = null;
			rl_howto.setVisibility(View.VISIBLE);
			if (mediaPlayer != null) mediaPlayer.stop();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		iv_image.setImageBitmap(null);
		if (convertedBitmap != null) {
			convertedBitmap.recycle();
			if (mediaPlayer.isPlaying()) mediaPlayer.stop();
			mediaPlayer.release();
		}
		
//		if (adView != null) {
//			adView.destroy();
//			adView = null;
//		}
		
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//menu disabled
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		/** from gallery **/
		if (requestCode == REQ_GALLERY && resultCode == RESULT_OK) {
			Uri selectedUri = data.getData();

			String path = selectedUri.toString();
	
			if (path != null) {
				startContentsSelectActivity(path);
			}
			else {
				Toast.makeText(this, "Intent Data is null", Toast.LENGTH_SHORT).show();
			}
		}
		
		/** from Camera **/
		else if (requestCode == REQ_CAMERA && resultCode == RESULT_OK) {

			Uri cameraImage = null;
			if (data == null) {
				//for common devices.
				cameraImage = cameraUri;
			}
			else {
				//Android Developers Reference Code
				cameraImage = data.getData();
			}
			
			//TODO ERROR 5
			if (cameraImage != null) {
				String path = cameraImage.getPath();
				startContentsSelectActivity(path);
			}
			else {
				Toast.makeText(this, getString(R.string.camera_failed), Toast.LENGTH_SHORT).show();
			}
		}
		
		else if (requestCode == REQ_CONTENTS && resultCode == RESULT_OK) {
			
			//received Contents and user text
			//running image process tasking
			String[] inputs = data.getStringArrayExtra(ContentSelectActivity.EXTRA_ARR_TAG);
			int type = data.getIntExtra(ContentSelectActivity.EXTRA_INT_TAG, -1);
			String imagePath = data.getStringExtra(ContentSelectActivity.EXTRA_PATH_TAG);
			
			ContentsData content = ContentsData.arrContents[type];
			
//			EasyTracker.getTracker().sendEvent("user_choice", "select_parody", content.getContent_tag(), 0l);
			
			RequestContents req = new RequestContents(content, imagePath, inputs);
			new ImageTask().execute(req);
		}
	}
	
	private void startContentsSelectActivity(final String path) {
		Intent i = new Intent(this, ContentSelectActivity.class);
		i.putExtra(ContentSelectActivity.EXTRA_PATH_TAG, path);
		startActivityForResult(i, REQ_CONTENTS);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
//			EasyTracker.getTracker().sendEvent("user_action", "image_save", "save", 0l);
			saveImage();
			break;

		case R.id.btn_gallery:
//			EasyTracker.getTracker().sendEvent("user_action", "image_load", "gallery", 0l);
			imageFromGallery();
			break;

		case R.id.btn_camera:
//			EasyTracker.getTracker().sendEvent("user_action", "image_load", "camera", 0l);
			imageFromCamera();
			break;
			
		case R.id.iv_image:
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
				}
				else {
					mediaPlayer.start();
				}
			}
			break;
			
		case R.id.btn_share:
			if (savedUri != null) {
//				EasyTracker.getTracker().sendEvent("user_action", "image_share", "share", 0l);
				KakaoShare.shareImageKakao(MainImageActivity.this, savedUri);
			}
			else {
				saveImage();
				isNeedShare = true;
			}
			break;
			
		case R.id.rl_info:
//			EasyTracker.getTracker().sendEvent("user_action", "open_info", "open_info", 0l);
			startActivity(new Intent(MainImageActivity.this, InfoActivity.class));
			break;
		}
	}
	

	private void imageFromGallery() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, getString(R.string.btn_gallery)), REQ_GALLERY);
//		startActivityForResult(intent, REQ_GALLERY);
	}

	private void imageFromCamera() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) mediaPlayer.pause();
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		cameraUri = MediaFileUtil.getOutputMediaFileUri(MediaFileUtil.MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		startActivityForResult(intent, REQ_CAMERA);
	}

	private void saveImage() {
		if (convertedBitmap == null) {
			Toast.makeText(MainImageActivity.this, getString(R.string.need_image), Toast.LENGTH_SHORT).show();
		}
		else {
			new SaveTask().execute(convertedBitmap);
		}
	}

	private void connectMediaScan(final String filename) {
		msc = new MediaScannerConnection(this, new MediaScannerConnectionClient() {

			@Override
			public void onScanCompleted(String path, Uri uri) {
				savedUri = uri;
				msc.disconnect();
				if (isNeedShare) {
					KakaoShare.shareImageKakao(MainImageActivity.this, uri);
				}
				isNeedShare = false;
			}

			@Override
			public void onMediaScannerConnected() {
				msc.scanFile(filename, "image/png");
			}
		});
		msc.connect();
	}
	
	private OnPreparedListener mediaPreparedListener = new OnPreparedListener() {
		
		@Override
		public void onPrepared(MediaPlayer mp) {
			mp.seekTo(0);
			mp.start();
		}
	};

	private class ImageTask extends AsyncTask<RequestContents, Integer, ResultsContents> {
		
		private RequestContents request;

		@Override
		protected void onPostExecute(ResultsContents result) {
			
			dialog.dismiss();
			
			if (result != null) {
				if (convertedBitmap != null) {
					convertedBitmap.recycle();
				}
				iv_image.setVisibility(View.GONE);
				iv_image.setImageBitmap(result.getBitmap());
				iv_image.setClickable(true);
				convertedBitmap = result.getBitmap();
				
				isNeedShare = false;
				savedUri = null;
				
				rl_howto.setVisibility(View.GONE);
				/*
				if (request.getContents_name() == ContentsData.POKE) {
					Toast toast = new Toast(MainImageActivity.this);
					ImageView iv = new ImageView(MainImageActivity.this);
					iv.setImageResource(R.drawable.poke_alert);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.setView(iv);
					toast.setDuration(1500);
					toast.show();
				}
				*/
				//before else if
				if (request.getContents_name() == ContentsData.JISIK) {
					Toast toast = new Toast(MainImageActivity.this);
					LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.splash_toast, null);
					ll.setBackgroundResource(R.drawable.jisik_splash);
					toast.setGravity(Gravity.FILL, 0, 0);
					toast.setView(ll);
					toast.setDuration(500);
					toast.show();
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							iv_image.setVisibility(View.VISIBLE);
						}
					}, 1000);
				}
				else if (request.getContents_name() == ContentsData.HUMAN) {
					Toast toast = new Toast(MainImageActivity.this);
					LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(R.layout.splash_toast, null);
					ll.setBackgroundResource(R.drawable.human_splash);
					toast.setGravity(Gravity.FILL, 0, 0);
					toast.setView(ll);
					toast.setDuration(500);
					toast.show();
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							iv_image.setVisibility(View.VISIBLE);
						}
					}, 1000);
				}
				else {					
					iv_image.setVisibility(View.VISIBLE);
					Toast.makeText(MainImageActivity.this, result.getBgm_info(), Toast.LENGTH_LONG).show();
				}
				
				if (mediaPlayer == null) {
					if (result.getBgm_resId() != 0) {
						mediaPlayer = MediaPlayer.create(MainImageActivity.this, result.getBgm_resId());
						mediaPlayer.setOnPreparedListener(mediaPreparedListener);
						mediaPlayer.setLooping(false);
					}
					else mediaPlayer = null;
				}
				else {
					if (result.getBgm_resId() != 0) {
						mediaPlayer.release();
						mediaPlayer = MediaPlayer.create(MainImageActivity.this, result.getBgm_resId());
						mediaPlayer.setOnPreparedListener(mediaPreparedListener);
						mediaPlayer.setLooping(false);
					}
					else mediaPlayer = null;
				}
				
//				EasyTracker.getTracker().sendEvent("user_action", "image_process", "success", 0l);
			}
			else {
				Toast.makeText(MainImageActivity.this, getResources().getString(R.string.toast_cantfile), Toast.LENGTH_SHORT).show();
//				EasyTracker.getTracker().sendEvent("user_action", "image_process", "failed", 0l);
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			iv_image.setImageBitmap(null);

			if (convertedBitmap != null) convertedBitmap.recycle();

			dialog = new ProgressDialog(MainImageActivity.this);
			dialog.setMessage(getResources().getString(R.string.dialog_loading));
			dialog.setCancelable(false);
			dialog.show();
			
			super.onPreExecute();
		}

		@Override
		protected ResultsContents doInBackground(RequestContents... params) {
			request = params[0];
			String path = request.getImage_path();
			
			if (!path.contains(".png") && !path.contains(".jpg")) {
				//real file directory searching.
				path = MediaFileUtil.getRealFilePath(path, getContentResolver());
				request.setImage_path(path);
				if (path == null) return null;
			}
			
			if (imageModify == null) {
				imageModify = new ImageModify(getResources());
			}
			
			ResultsContents result = imageModify.generateContents(request);
			
			return result;
		}
	}

	private class SaveTask extends AsyncTask<Bitmap, Integer, String> {

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			if (result != null) {
				Toast.makeText(MainImageActivity.this, getResources().getString(R.string.toast_saved), Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(MainImageActivity.this, getResources().getString(R.string.toast_failed), Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			dialog.setMessage(getResources().getString(R.string.dialog_saving));
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Bitmap... params) {

			File imageFile = MediaFileUtil.getOutputMediaFile(MediaFileUtil.MEDIA_TYPE_IMAGE);
			OutputStream out = null;
			
			//TODO BUG 3
			if (imageFile == null) return null;

			try {
				imageFile.createNewFile();
				out = new FileOutputStream(imageFile);
				params[0].compress(CompressFormat.PNG, 100, out);
				out.close();				
				connectMediaScan(imageFile.toString());

			} catch (IOException e) {
				e.printStackTrace();
			}
			return imageFile.toString();
		}
	}
	
	
	
	/*
	 * 
	 * naver adpost
	 */

//	@Override
//	public void onReceive(int arg0) {
////		Log.i("adPost", "onReceive : " + String.valueOf(arg0));
//	}
}
