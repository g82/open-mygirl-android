package com.seokceed.openmygirl.image;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

public class MediaFileUtil {
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	public static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	public static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "MyGirl");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("file error", "failed to create directory");
	            return null;
	        }
	    }
	    

	    // Create a media file name
	    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
	    String timeStamp = format.format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "MyGirl_"+ timeStamp + ".png");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }
	    
	    return mediaFile;
	}
	
	public static final String getRealFilePath (String uriPath, ContentResolver resolver) {
		
		String proj[] = {MediaStore.Images.Media.DATA};
		Cursor cursor = resolver.query(Uri.parse(uriPath), proj, null, null, null);
		int index = 0;
		
		//TODO ERROR 2
		try {
			index = cursor.getColumnIndexOrThrow(Media.DATA);
		}
		catch (IllegalArgumentException e) {
			return null;
		}
		cursor.moveToFirst();
		String filePath = cursor.getString(index);
		cursor.close();
		
		return filePath;
	}
	
	public static Bitmap setRotateBitmap(Bitmap bitmap, int degrees) {
		if (degrees != 0 && bitmap != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float)bitmap.getWidth()/2, (float)bitmap.getHeight()/2);
			
			Bitmap rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
			
			if (bitmap != rotated) {
				bitmap.recycle();
				bitmap = rotated;
			}
		}
		
		return bitmap;
	}
	
	public static int getExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
		
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException e) {
			Log.i("exif error", e.toString());
			e.printStackTrace();
		}
		
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			
			if (orientation != -1) {
				switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;

				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
					
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
				
			}
		}
		return degree;
	}

}
