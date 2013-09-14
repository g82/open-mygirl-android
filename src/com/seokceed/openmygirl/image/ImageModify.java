package com.seokceed.openmygirl.image;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.seokceed.openmygirl.R;
import com.seokceed.openmygirl.contents.ContentsData;
import com.seokceed.openmygirl.contents.RequestContents;
import com.seokceed.openmygirl.contents.ResultsContents;

public class ImageModify {

	public static final int CONVERT_GRAYSCALE = 1;
	public static final int CONVERT_SEPIA = 2;

	private static final float TARGET_LANDSCAPE_WIDTH = 960.0f;
	private static final float TARGET_PORTRAIT_WIDTH = 720.0f;

	public static final String TAG = "debug";

	private Resources res;

	public ImageModify(Resources res) {
		this.res = res;
	}

	public final ResultsContents generateContents(RequestContents request) {

		// extract original image & size
		// if rotated image, rotate original bitmap.
		BitmapFactory.Options opts = new Options();
		opts.inPreferredConfig = Config.RGB_565;
		opts.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(request.getImage_path(), opts);

		if (opts.outWidth == -1 || opts.outHeight == -1) {
			return null;
		} else {
			if (opts.outWidth >= 1800 || opts.outHeight >= 1800) {
				opts.inSampleSize = 2;
			} else if (opts.outWidth >= 3000 || opts.outHeight >= 3000) {
				opts.inSampleSize = 4;
			}
		}
		opts.inJustDecodeBounds = false;

		Bitmap originBmp = BitmapFactory.decodeFile(request.getImage_path(),
				opts);

		// LG Optimus BIG, Black is can't load bitmap
		if (originBmp == null)
			return null;

		int rotated = MediaFileUtil.getExifOrientation(request.getImage_path());

		if (rotated != 0) {
			originBmp = MediaFileUtil.setRotateBitmap(originBmp, rotated);
		}

		if (originBmp == null)
			return null;

		int original_width, original_height;
		original_height = originBmp.getHeight();
		original_width = originBmp.getWidth();

		if (original_width <= 0 || original_height <= 0)
			return null;

		// check original bitmap is landscape or portrait and largeimage >
		// TARGET_WIDTH
		boolean isLandScape = (original_width > original_height) ? true : false;

		boolean isLargeImage = false;
		if (isLandScape) {
			isLargeImage = (original_width > TARGET_LANDSCAPE_WIDTH) ? true
					: false;
		} else
			isLargeImage = (original_width > TARGET_PORTRAIT_WIDTH) ? true
					: false;

		ResultsContents results = null;

		switch (request.getContents_name()) {
		case BENE:
			results = generateBene(originBmp, original_width, original_height,
					isLandScape, isLargeImage);
			break;

		case HUMAN:
			results = generateHuman(originBmp, request, original_width,
					original_height, isLandScape, isLargeImage);
			break;

		/*
		 * case LOL: results = generateLOL(originBmp, request, original_width,
		 * original_height, isLandScape, isLargeImage); break; case POKE:
		 * results = generatePocketmon(originBmp, request, original_width,
		 * original_height, isLandScape, isLargeImage); break;
		 */
		case SINGROOM:
			results = generateSingroom(originBmp, request, original_width,
					original_height, isLandScape, isLargeImage);
			break;

//		case WOOPAROO:
//			results = generateWooparoo(originBmp, request, original_width,
//					original_height, isLandScape, isLargeImage);
//			break;

		case JISIK:
			results = generateJisik(originBmp, request, original_width,
					original_height, isLandScape, isLargeImage);
			break;
		}
//		case OLLEH:
//			results = generateOLLEH(originBmp, request, original_width,
//					original_height, isLandScape, isLargeImage);
//			break;
//		}

		return results;
	}

	/*
	private static final float WOOPAROO_CENTER_X = 286.5f;
	private static final float WOOPAROO_CENTER_Y = 248.5f;

	private static final float WOOPAROO_TARGET_WIDTH = 290f;
	private static final float WOOPAROO_TARGET_HEIGHT = 300f;

	private static final float WOOPAROO_NAME_CENTER_X = 293.5f;
	private static final float WOOPAROO_NAME_CENTER_Y = 430.5f;

	private static final float WOOPAROO_SECREATION_CENTER_X = 607f;
	private static final float WOOPAROO_SECREATION_CENTER_Y = 215f;

	private static final float WOOPAROO_LOCA_CENTER_X = 750f;
	private static final float WOOPAROO_LOCA_CENTER_Y = 245f;

	private static final float WOOPAROO_AGE_CENTER_X = 631f;
	private static final float WOOPAROO_AGE_CENTER_Y = 301f;

	private static final float WOOPAROO_TXT_CENTER_X = 520f;
	private static final float WOOPAROO_TXT_CENTER_Y = 350f;

	private static final float WOOPAROO_LIKE_CENTER_X = 293f;
	private static final float WOOPAROO_LIKE_CENTER_Y = 560f;

	private static final float WOOPAROO_PER_CENTER_X = 622f;
	private static final float WOOPAROO_PER_CENTER_Y = 247f;

	private static final float WOOPAROO_MOUNT_CENTER_X = 607f;
	private static final float WOOPAROO_MOUNT_CENTER_Y = 247f;

	private static final float WOOPAROO_TEXT_NAME_SIZE = 30f;
	private static final float WOOPAROO_TEXT_LOCA_SIZE = 24f;
	private static final float WOOPAROO_TEXT_SECREATION_SIZE = 20f;
	private static final float WOOPAROO_TEXT_AGE_SIZE = 25f;
	private static final float WOOPAROO_TEXT_LIKE_SIZE = 24f;

	private ResultsContents generateWooparoo(Bitmap originBmp,
			RequestContents request, int original_width, int original_height,
			boolean isLandScape, boolean isLargeImage) {

		float resizeRatio = 1.0f;
		if (isLandScape)
			resizeRatio = (WOOPAROO_TARGET_HEIGHT > original_height) ? original_height
					/ WOOPAROO_TARGET_HEIGHT
					: WOOPAROO_TARGET_HEIGHT / original_height;

		else
			resizeRatio = (WOOPAROO_TARGET_WIDTH > original_width) ? original_width
					/ WOOPAROO_TARGET_WIDTH
					: WOOPAROO_TARGET_WIDTH / original_width;

		original_width *= resizeRatio;
		original_height *= resizeRatio;

		Bitmap resized_picBmp = Bitmap.createScaledBitmap(originBmp,
				original_width, original_height, true);

		Bitmap canvasBitmap = Bitmap.createBitmap(960, 640, Config.RGB_565);
		Canvas canvas = new Canvas(canvasBitmap);
		canvas.drawColor(Color.BLACK);

		Paint aaPaint = new Paint();
		aaPaint.setAntiAlias(true);

		canvas.drawBitmap(resized_picBmp,
				WOOPAROO_CENTER_X - (resized_picBmp.getWidth() / 2),
				WOOPAROO_CENTER_Y - (resized_picBmp.getHeight() / 2), aaPaint);

		int frameRID = Wooparoo.getRandomElementFrame();

		Options opts = new Options();
		opts.inDensity = resized_picBmp.getDensity();
		opts.inPreferredConfig = Config.RGB_565;
		Bitmap frameBitmap = BitmapFactory.decodeResource(res, frameRID, opts);

		canvas.drawBitmap(frameBitmap, 0, 0, aaPaint);

		String[] args = request.getArgs();
		String name = args[0];
		String age = args[1] + res.getString(R.string.years);
		String loca = args[2];
		String like = args[3];
		String text = args[4];
		String secretion = args[5];

		Typeface fonts = Typeface.createFromAsset(res.getAssets(),
				"NanumGothicBold.otf");

		Paint textPaint = new Paint(aaPaint);
		textPaint.setTypeface(fonts);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(Color.argb(128, 58, 44, 26));
		textPaint.setStrokeWidth(8f);
		textPaint.setStyle(Style.STROKE);
		textPaint.setTextSize(WOOPAROO_TEXT_NAME_SIZE);
		canvas.drawText(name, WOOPAROO_NAME_CENTER_X, WOOPAROO_NAME_CENTER_Y,
				textPaint);

		textPaint.reset();
		textPaint.setTypeface(fonts);
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(WOOPAROO_TEXT_NAME_SIZE);
		canvas.drawText(name, WOOPAROO_NAME_CENTER_X, WOOPAROO_NAME_CENTER_Y,
				textPaint);

		textPaint.reset();
		textPaint.setTypeface(fonts);
		textPaint.setAntiAlias(true);
		textPaint.setStyle(Style.FILL_AND_STROKE);
		textPaint.setStrokeWidth(1f);
		textPaint.setTextAlign(Align.RIGHT);
		textPaint.setColor(Color.rgb(147, 59, 0));
		textPaint.setTextSize(WOOPAROO_TEXT_SECREATION_SIZE);
		canvas.drawText(secretion, WOOPAROO_SECREATION_CENTER_X,
				WOOPAROO_SECREATION_CENTER_Y, textPaint);

		textPaint.reset();
		textPaint.setTypeface(fonts);
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(Color.rgb(58, 44, 26));
		textPaint.setTextSize(WOOPAROO_TEXT_LOCA_SIZE);
		canvas.drawText(loca, WOOPAROO_LOCA_CENTER_X, WOOPAROO_LOCA_CENTER_Y,
				textPaint);

		textPaint.reset();
		textPaint.setTypeface(fonts);
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.rgb(4, 121, 180));
		textPaint.setTextAlign(Align.LEFT);
		textPaint.setTextSize(WOOPAROO_TEXT_AGE_SIZE);
		canvas.drawText(age, WOOPAROO_AGE_CENTER_X, WOOPAROO_AGE_CENTER_Y,
				textPaint);

		textPaint.reset();
		textPaint.setTypeface(fonts);
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.rgb(58, 44, 26));
		textPaint.setTextAlign(Align.LEFT);
		textPaint.setTextSize(WOOPAROO_TEXT_AGE_SIZE);
		// need newLine processing
		Rect bounds = new Rect();
		String lines[] = text.split("\n");
		float yOff = WOOPAROO_TXT_CENTER_Y;
		for (int i = 0; i < lines.length; i++) {
			canvas.drawText(lines[i], WOOPAROO_TXT_CENTER_X, yOff, textPaint);
			textPaint.getTextBounds(lines[i], 0, lines[i].length(), bounds);
			yOff += (bounds.height() * 1.2);
		}

		textPaint.reset();
		textPaint.setTypeface(fonts);
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.rgb(231, 186, 41));
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(WOOPAROO_TEXT_LIKE_SIZE);
		canvas.drawText(like, WOOPAROO_LIKE_CENTER_X, WOOPAROO_LIKE_CENTER_Y,
				textPaint);

		textPaint.reset();
		textPaint.setTypeface(fonts);
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.rgb(4, 121, 180));
		textPaint.setTextAlign(Align.RIGHT);
		textPaint.setTextSize(WOOPAROO_TEXT_AGE_SIZE);
		canvas.drawText(Wooparoo.getRandomMountText(res),
				WOOPAROO_MOUNT_CENTER_X, WOOPAROO_MOUNT_CENTER_Y, textPaint);

		textPaint.reset();
		textPaint.setTypeface(fonts);
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.rgb(58, 44, 26));
		textPaint.setTextAlign(Align.LEFT);
		textPaint.setTextSize(WOOPAROO_TEXT_AGE_SIZE);
		canvas.drawText(Wooparoo.getRandomPerText(res), WOOPAROO_PER_CENTER_X,
				WOOPAROO_PER_CENTER_Y, textPaint);

		String meta_info[] = res.getStringArray(R.array.meta_wooparoo);
		ResultsContents result = new ResultsContents(0,
				meta_info[2], canvasBitmap);
//		ResultsContents result = new ResultsContents(R.raw.woo_bgm,
//				meta_info[2], canvasBitmap);

		originBmp.recycle();
		resized_picBmp.recycle();
		frameBitmap.recycle();
		originBmp = null;
		resized_picBmp = null;
		frameBitmap = null;

		return result;
	}
	*/

	private ResultsContents generateJisik(Bitmap originBmp,
			RequestContents request, int original_width, int original_height,
			boolean isLandScape, boolean isLargeImage) {

		float resizeRatio = 1.0f;
		Bitmap resized_source_bmp = null;

		final float max_width = 460;
		final float max_height = 500;

		if (isLandScape) {
			if (max_width > original_width) {
				// is small size
				resizeRatio = original_width / max_width;
			} else {
				resizeRatio = max_width / original_width;
			}
		} else {
			if (max_height > original_height) {
				// is small size
				resizeRatio = original_height / max_height;
			} else {
				resizeRatio = max_height / original_height;
			}
		}

		original_width *= resizeRatio;
		original_height *= resizeRatio;

		resized_source_bmp = Bitmap.createScaledBitmap(originBmp,
				original_width, original_height, true);
		originBmp.recycle();

		Options opts = new Options();
		opts.inDensity = resized_source_bmp.getDensity();
		opts.inPreferredConfig = Config.RGB_565;
		Bitmap backBitmap = BitmapFactory.decodeResource(res,
				R.drawable.jisik_land, opts);
		Bitmap canvasBitmap = backBitmap.copy(Config.RGB_565, true);

		backBitmap.recycle();

		final float canvasWidth = 960;
		final float canvasHeight = 640;

		int imageWidth = resized_source_bmp.getWidth();
		int imageHeight = resized_source_bmp.getHeight();

		float textSize = 40.0f;
		float strokeWidth = 2.0f;

		Typeface fonts = Typeface.createFromAsset(res.getAssets(),
				"NanumMyeongjoBold.otf");
		Typeface italic = Typeface.create(fonts, Typeface.ITALIC);

		Canvas canvas = new Canvas(canvasBitmap);

		// 1. drawing grayscale
		Paint graypaint = new Paint();
		graypaint.setAntiAlias(true);
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0.08f);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		graypaint.setColorFilter(f);
		canvas.drawBitmap(resized_source_bmp, 480 - (imageWidth / 2),
				320 - imageHeight * 0.6f, graypaint);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTypeface(italic);
		String[] args = request.getArgs();

		paint.setTextAlign(Align.CENTER);
		paint.setTextScaleX(0.9f);
		paint.setColor(Color.WHITE);
		paint.setTextSize(textSize);
		paint.setTextSkewX(-0.18f);

		Paint borderPaint = new Paint(paint);
		borderPaint.setColor(Color.BLACK);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(strokeWidth);
		borderPaint.setShadowLayer(10.0f, 0.0f, 0.0f, Color.BLACK);

		float textTopPoint = canvasHeight * 0.7f;

		if (!args[1].equals(""))
			args[1] = "\"" + args[1];

		for (int i = 1; i < args.length; i++) {
			if (!args[i].equals("")) {
				if (i == 3) {
					args[i] = args[i] + "\"";
					canvas.drawText(args[i], 480, textTopPoint, borderPaint);
					canvas.drawText(args[i], 480, textTopPoint, paint);
					textTopPoint += 40.0f;
					break;
				}
				if (args[i + 1] == null || args[i + 1].equals("")) {
					args[i] = args[i] + "\"";
					canvas.drawText(args[i], 480, textTopPoint, borderPaint);
					canvas.drawText(args[i], 480, textTopPoint, paint);
					textTopPoint += 40.0f;
					break;
				} else {
					canvas.drawText(args[i], 480, textTopPoint, borderPaint);
					canvas.drawText(args[i], 480, textTopPoint, paint);
					textTopPoint += 40.0f;
				}
			}
		}

		paint.setTextSize(30f);
		borderPaint.setTextSize(30f);
		canvas.drawText("-" + args[0], 480, textTopPoint, borderPaint);
		canvas.drawText("-" + args[0], 480, textTopPoint, paint);

		String bgmInfo = res.getStringArray(R.array.meta_jisik)[2];
		// ResultsContents result = new ResultsContents(R.raw.bgm_jisik,
		// bgmInfo, canvasBitmap);
		ResultsContents result = new ResultsContents(0, bgmInfo, canvasBitmap);

		return result;
	}

	private ResultsContents generateSingroom(Bitmap originBmp,
			RequestContents request, int original_width, int original_height,
			boolean isLandScape, boolean isLargeImage) {

		float resizeRatio = 1.0f;
		Bitmap resized_source_bmp = null;
		if (isLargeImage) {
			resizeRatio = isLandScape ? TARGET_LANDSCAPE_WIDTH / original_width
					: TARGET_PORTRAIT_WIDTH / original_width;
			original_width *= resizeRatio;
			original_height *= resizeRatio;
			// resize original canvas part
			resized_source_bmp = Bitmap.createScaledBitmap(originBmp,
					original_width, original_height, true);
			originBmp.recycle();
		} else {
			resizeRatio = isLandScape ? original_width / TARGET_LANDSCAPE_WIDTH
					: original_width / TARGET_PORTRAIT_WIDTH;
			resized_source_bmp = originBmp.copy(Config.RGB_565, true);
			originBmp.recycle();
		}

		final int canvasWidth = resized_source_bmp.getWidth();
		final int canvasHeight = resized_source_bmp.getHeight();

		Options opts = new Options();
		opts.inDensity = resized_source_bmp.getDensity();
		opts.inPreferredConfig = Config.RGB_565;

		float textSize = 50.0f;
		float strokeWidth = 9.0f;
		if (!isLargeImage) {
			textSize *= resizeRatio;
			strokeWidth *= resizeRatio;
		}

		Typeface fonts = Typeface.createFromAsset(res.getAssets(),
				"BareunBatangOTFPro-3.otf");
		Typeface bold = Typeface.create(fonts, Typeface.BOLD);

		Paint commonPaint = new Paint();
		commonPaint.setAntiAlias(true);
		commonPaint.setTypeface(bold);

		Paint textPaint = new Paint(commonPaint);
		textPaint.setTextSize(textSize);
		textPaint.setTextAlign(Align.LEFT);
		textPaint.setColor(Color.rgb(161, 113, 231));

		Paint borderPaint = new Paint(textPaint);
		borderPaint.setStrokeWidth(strokeWidth);
		borderPaint.setStrokeCap(Cap.SQUARE);
		borderPaint.setColor(Color.WHITE);
		borderPaint.setStyle(Style.STROKE);

		Canvas canvas = new Canvas(resized_source_bmp);
		String[] args = request.getArgs();
		String num = args[0];
		String title = args[1];
		String singer = args[2];
		String first_part = args[3];
		String second_part = args[4];

		String topText = num + " " + title + " - " + singer;

		Paint rectPaint = new Paint();
		rectPaint.setColor(Color.BLACK);
		rectPaint.setStyle(Style.FILL);
		rectPaint.setAntiAlias(true);
		canvas.drawRect(0.0f, canvasHeight * 0.92f, (float) canvasWidth,
				(float) canvasHeight, rectPaint);

		if (isLandScape) {

			Bitmap bar = BitmapFactory.decodeResource(res,
					R.drawable.song_land, opts);

			float barHeight = 0.0f;
			if (!isLargeImage) {
				Bitmap scaledBar = Bitmap.createScaledBitmap(bar,
						(int) (bar.getWidth() * resizeRatio),
						(int) (bar.getHeight() * resizeRatio), true);
				canvas.drawBitmap(scaledBar, 0, 0, new Paint());
				barHeight = scaledBar.getHeight();
				bar.recycle();
				scaledBar.recycle();
			} else {
				canvas.drawBitmap(bar, 0, 0, new Paint());
				barHeight = bar.getHeight();
				bar.recycle();
			}

			// 1st part
			borderPaint.setStrokeWidth(strokeWidth * 1.2f);

			if (first_part.length() >= 10) {
				canvas.drawText(first_part, canvasWidth * 0.15f,
						canvasHeight * 0.85f, borderPaint);
				canvas.drawText(first_part, canvasWidth * 0.15f,
						canvasHeight * 0.85f, textPaint);
			}

			else {
				textPaint.setTextAlign(Align.CENTER);
				borderPaint.setTextAlign(Align.CENTER);
				canvas.drawText(first_part, canvasWidth * 0.5f,
						canvasHeight * 0.85f, borderPaint);
				canvas.drawText(first_part, canvasWidth * 0.5f,
						canvasHeight * 0.85f, textPaint);
			}

			Rect bounds = new Rect();
			textPaint.getTextBounds(first_part, 0, first_part.length(), bounds);
			float secondTop = (canvasHeight * 0.85f) + (bounds.height() * 1.3f);

			// 2nd part
			borderPaint.setColor(Color.BLACK);
			textPaint.setColor(Color.rgb(164, 252, 228));
			borderPaint.setStrokeWidth(strokeWidth);

			if (first_part.length() >= 10) {
				textPaint.setTextAlign(Align.RIGHT);
				borderPaint.setTextAlign(Align.RIGHT);
				canvas.drawText(second_part, canvasWidth * 0.85f, secondTop,
						borderPaint);
				canvas.drawText(second_part, canvasWidth * 0.85f, secondTop,
						textPaint);
			} else {
				textPaint.setTextAlign(Align.CENTER);
				borderPaint.setTextAlign(Align.CENTER);
				canvas.drawText(second_part, canvasWidth * 0.5f, secondTop,
						borderPaint);
				canvas.drawText(second_part, canvasWidth * 0.5f, secondTop,
						textPaint);
			}

			if (topText.length() >= 20) {
				topText = num + " " + title + " - " + "...";
			}
			// top part
			textPaint.setColor(Color.WHITE);
			textPaint.setTextAlign(Align.LEFT);
			textPaint.setTextSize(textSize * 0.70f);
			textPaint.setTypeface(Typeface.DEFAULT_BOLD);
			canvas.drawText(topText, canvasWidth * 0.15f, barHeight * 0.75f,
					textPaint);

		} else {

			Bitmap bar = BitmapFactory.decodeResource(res,
					R.drawable.song_port, opts);

			float barHeight = 1.0f;
			if (!isLargeImage) {
				Bitmap scaledBar = Bitmap.createScaledBitmap(bar,
						(int) (bar.getWidth() * resizeRatio),
						(int) (bar.getHeight() * resizeRatio), true);
				canvas.drawBitmap(scaledBar, 0, 0, new Paint());
				barHeight = scaledBar.getHeight();
				bar.recycle();
				scaledBar.recycle();
			} else {
				canvas.drawBitmap(bar, 0, 0, new Paint());
				barHeight = bar.getHeight();
				bar.recycle();
			}

			// 1st part
			borderPaint.setStrokeWidth(strokeWidth * 1.2f);

			if (first_part.length() >= 10) {
				canvas.drawText(first_part, canvasWidth * 0.10f,
						canvasHeight * 0.85f, borderPaint);
				canvas.drawText(first_part, canvasWidth * 0.10f,
						canvasHeight * 0.85f, textPaint);
			}

			else {
				textPaint.setTextAlign(Align.CENTER);
				borderPaint.setTextAlign(Align.CENTER);
				canvas.drawText(first_part, canvasWidth * 0.5f,
						canvasHeight * 0.85f, borderPaint);
				canvas.drawText(first_part, canvasWidth * 0.5f,
						canvasHeight * 0.85f, textPaint);
			}

			Rect bounds = new Rect();
			textPaint.getTextBounds(first_part, 0, first_part.length(), bounds);
			float secondTop = (canvasHeight * 0.85f) + (bounds.height() * 1.3f);

			// 2nd part
			borderPaint.setColor(Color.BLACK);
			textPaint.setColor(Color.rgb(164, 252, 228));
			borderPaint.setStrokeWidth(strokeWidth);

			if (first_part.length() >= 10) {
				textPaint.setTextAlign(Align.RIGHT);
				borderPaint.setTextAlign(Align.RIGHT);
				canvas.drawText(second_part, canvasWidth * 0.90f, secondTop,
						borderPaint);
				canvas.drawText(second_part, canvasWidth * 0.90f, secondTop,
						textPaint);
			} else {
				textPaint.setTextAlign(Align.CENTER);
				borderPaint.setTextAlign(Align.CENTER);
				canvas.drawText(second_part, canvasWidth * 0.5f, secondTop,
						borderPaint);
				canvas.drawText(second_part, canvasWidth * 0.5f, secondTop,
						textPaint);
			}

			if (topText.length() >= 20) {
				topText = num + " " + title + " - " + "...";
			}

			// top part
			textPaint.setColor(Color.WHITE);
			textPaint.setTextAlign(Align.LEFT);
			textPaint.setTextSize(textSize * 0.70f);
			textPaint.setTypeface(Typeface.DEFAULT_BOLD);
			canvas.drawText(topText, canvasWidth * 0.17f, barHeight * 0.75f,
					textPaint);

		}

		String[] meta_data = res.getStringArray(R.array.meta_singroom);
		// ResultsContents result= new ResultsContents(R.raw.sing_bgm,
		// meta_data[2], resized_source_bmp);
		ResultsContents result = new ResultsContents(0, meta_data[2],
				resized_source_bmp);
		return result;
	}

	// private ResultsContents generateSponge(Bitmap originBmp,
	// RequestContents request, int original_width, int original_height,
	// boolean isLandScape, boolean isLargeImage) {
	//
	// float resizeRatio = 1.0f;
	// Bitmap resized_source_bmp = null;
	// if (isLargeImage) {
	// resizeRatio = isLandScape ? TARGET_LANDSCAPE_WIDTH / original_width :
	// TARGET_PORTRAIT_WIDTH / original_width;
	// original_width *= resizeRatio;
	// original_height *= resizeRatio;
	// //resize original canvas part
	// resized_source_bmp = Bitmap.createScaledBitmap(originBmp, original_width,
	// original_height, true);
	// originBmp.recycle();
	// }
	// else {
	// resizeRatio = isLandScape ? original_width / TARGET_LANDSCAPE_WIDTH :
	// original_width / TARGET_PORTRAIT_WIDTH;
	// resized_source_bmp = originBmp.copy(Config.RGB_565, true);
	// originBmp.recycle();
	// }
	//
	// final int canvasWidth = resized_source_bmp.getWidth();
	// final int canvasHeight = resized_source_bmp.getHeight();
	//
	// Options opts = new Options();
	// opts.inDensity = resized_source_bmp.getDensity();
	// opts.inPreferredConfig = Config.RGB_565;
	//
	// Bitmap bottom_bmp = BitmapFactory.decodeResource(res,
	// R.drawable.sponge_land_bottom, opts);
	//
	// Canvas canvas = new Canvas(resized_source_bmp);
	//
	// ResultsContents result= new ResultsContents(R.raw.bene_bgm, "hing",
	// resized_source_bmp);
	// return result;
	// }

	/*
	 * 
	 * private ResultsContents generatePocketmon(Bitmap originBmp,
	 * RequestContents request, int original_width, int original_height, boolean
	 * isLandScape, boolean isLargeImage) {
	 * 
	 * final float limit_width = 500.0f; final float limit_height = 380.0f;
	 * 
	 * float resizeRatio = 1.0f;
	 * 
	 * if (isLandScape) { isLargeImage = (original_width > limit_width) ? true :
	 * false; resizeRatio = isLargeImage ? limit_width / original_width :
	 * original_width / limit_width; } else { isLargeImage = (original_height >
	 * limit_height) ? true : false; resizeRatio = isLargeImage ? limit_height /
	 * original_height : original_height / limit_height; }
	 * 
	 * Bitmap resizedBitmap = Bitmap.createScaledBitmap(originBmp,
	 * (int)(original_width * resizeRatio), (int)(original_height *
	 * resizeRatio), true);
	 * 
	 * Options opts = new Options(); opts.inDensity =
	 * resizedBitmap.getDensity(); opts.inPreferredConfig = Config.RGB_565;
	 * 
	 * //api 11 higher available // opts.inMutable = true;
	 * 
	 * Bitmap oriBitmap = BitmapFactory.decodeResource(res,
	 * R.drawable.poke_back, opts); Bitmap canvasBitmap =
	 * oriBitmap.copy(Config.RGB_565, true); oriBitmap.recycle();
	 * 
	 * Canvas canvas = new Canvas(canvasBitmap);
	 * 
	 * Paint paint = new Paint(); paint.setAntiAlias(true);
	 * paint.setTextScaleX(0.90f); float rightTopX = canvasBitmap.getWidth() *
	 * 0.96f; float rightTopY = canvasBitmap.getHeight() * 0.05f;
	 * 
	 * canvas.drawBitmap(resizedBitmap, rightTopX - resizedBitmap.getWidth(),
	 * rightTopY, paint);
	 * 
	 * String[] arrText = request.getArgs(); //level paint.setTextSize(35.0f);
	 * canvas.drawText(arrText[1], 185, 104, paint);
	 * 
	 * //nick paint.setTextSize(50.0f); canvas.drawText(arrText[0], 54, 60,
	 * paint);
	 * 
	 * //text paint.setTextSize(40.0f); canvas.drawText(arrText[0]+ "의 " +
	 * arrText[2]+"!", 50, 590, paint);
	 * 
	 * String effect = null; Random r = new Random(); int random = r.nextInt(2);
	 * effect = random ==1 ? res.getString(R.string.poke_effect_good) :
	 * res.getString(R.string.poke_effect_bad); canvas.drawText(effect, 50, 660,
	 * paint);
	 * 
	 * String[] meta_data = res.getStringArray(R.array.meta_poke); //
	 * ResultsContents result = new ResultsContents(R.raw.bene_bgm,
	 * meta_data[2], canvasBitmap); ResultsContents result = new
	 * ResultsContents(0, meta_data[2], canvasBitmap);
	 * 
	 * originBmp.recycle(); resizedBitmap.recycle(); return result; }
	 * 
	 * private ResultsContents generateLOL(Bitmap originBmp, RequestContents
	 * request, int original_width, int original_height, boolean isLandScape,
	 * boolean isLargeImage) {
	 * 
	 * float resizeRatio = 1.0f; Bitmap resizedBmp = null;
	 * 
	 * if (isLargeImage) { resizeRatio = isLandScape ? TARGET_LANDSCAPE_WIDTH /
	 * original_width : TARGET_PORTRAIT_WIDTH / original_width; original_width
	 * *= resizeRatio; original_height *= resizeRatio; //resize original canvas
	 * part resizedBmp = Bitmap.createScaledBitmap(originBmp, original_width,
	 * original_height, true); originBmp.recycle(); } else { resizeRatio =
	 * isLandScape ? original_width / TARGET_LANDSCAPE_WIDTH : original_width /
	 * TARGET_PORTRAIT_WIDTH; resizedBmp = originBmp.copy(Config.RGB_565, true);
	 * originBmp.recycle(); }
	 * 
	 * final int canvasWidth = resizedBmp.getWidth(); final int canvasHeight =
	 * resizedBmp.getHeight();
	 * 
	 * Canvas canvas = new Canvas(resizedBmp);
	 * 
	 * // 1. drawing grayscale Paint paint = new Paint();
	 * paint.setAntiAlias(true); ColorMatrix cm = new ColorMatrix();
	 * cm.setSaturation(0); ColorMatrixColorFilter f = new
	 * ColorMatrixColorFilter(cm); paint.setColorFilter(f);
	 * canvas.drawBitmap(resizedBmp, 0, 0, paint); paint.reset();
	 * 
	 * 
	 * //2 . drawing interface Options opts = new Options(); opts.inDensity =
	 * originBmp.getDensity();
	 * 
	 * Bitmap bitmap_interface = BitmapFactory.decodeResource(res,
	 * R.drawable.lol_back, opts); //TODO FIRST DOWN if (bitmap_interface ==
	 * null) return null;
	 * 
	 * if (isLandScape) { if (isLargeImage) {
	 * canvas.drawBitmap(bitmap_interface, 0, canvasHeight -
	 * bitmap_interface.getHeight(), paint); } else { int dstWidth = (int)
	 * (bitmap_interface.getWidth() * resizeRatio); int dstHeight = (int)
	 * (bitmap_interface.getHeight() * resizeRatio); Bitmap
	 * bitmap_resized_interface = Bitmap.createScaledBitmap(bitmap_interface,
	 * dstWidth, dstHeight, true); canvas.drawBitmap(bitmap_resized_interface,
	 * 0, canvasHeight - dstHeight, paint); bitmap_resized_interface.recycle();
	 * } } else { int targetHeight = (int) (canvasHeight * 0.52); int height =
	 * bitmap_interface.getHeight(); float ratio = (targetHeight >= height) ?
	 * targetHeight / height : height / targetHeight; Bitmap
	 * bitmap_resized_interface = Bitmap.createScaledBitmap(bitmap_interface,
	 * (int) (bitmap_interface.getWidth() * ratio), targetHeight, true);
	 * canvas.drawBitmap(bitmap_resized_interface, 0, canvasHeight -
	 * targetHeight, paint); bitmap_resized_interface.recycle(); }
	 * bitmap_interface.recycle();
	 * 
	 * 
	 * //3. drawing text // text drawing two part. // original white text &
	 * borderline color text.
	 * 
	 * float textSize = 50.0f; float strokeWidth = 9.0f; if (!isLargeImage) {
	 * textSize *= resizeRatio; strokeWidth *= resizeRatio; }
	 * 
	 * Typeface sans = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
	 * paint.setAntiAlias(true);
	 * 
	 * Paint textPaint = paint; textPaint.setTypeface(sans);
	 * textPaint.setTextSize(textSize); textPaint.setColor(Color.WHITE);
	 * textPaint.setTextAlign(Align.CENTER);
	 * 
	 * Paint borderPaint = new Paint(textPaint);
	 * borderPaint.setStrokeWidth(strokeWidth);
	 * borderPaint.setStrokeCap(Cap.SQUARE); borderPaint.setColor(Color.rgb(180,
	 * 0, 0)); borderPaint.setStyle(Style.STROKE);
	 * 
	 * String name = request.getArgs()[0];
	 * 
	 * if (isLandScape) { String fullText = name +
	 * res.getString(R.string.lol_crazy); float textWidth =
	 * textPaint.measureText(fullText); float textRatio = (canvasWidth <=
	 * textWidth) ? textWidth/ canvasHeight: 1.0f ; textSize *= textRatio;
	 * 
	 * textPaint.setTextSize(textSize); borderPaint.setTextSize(textSize);
	 * 
	 * canvas.drawText(fullText, canvasWidth / 2, canvasHeight * 0.10f,
	 * borderPaint); canvas.drawText(fullText, canvasWidth / 2, canvasHeight *
	 * 0.10f, textPaint); } else { String fullText = name +
	 * res.getString(R.string.lol_crazy); float textWidth =
	 * textPaint.measureText(fullText); float textRatio = (canvasWidth <=
	 * textWidth) ? textWidth / canvasHeight : 1.0f ; textSize *= textRatio;
	 * 
	 * textPaint.setTextSize(textSize); borderPaint.setTextSize(textSize);
	 * 
	 * canvas.drawText(fullText, canvasWidth / 2, canvasHeight * 0.10f,
	 * borderPaint); canvas.drawText(fullText, canvasWidth / 2, canvasHeight *
	 * 0.10f, textPaint); }
	 * 
	 * String[] metadata = res.getStringArray(R.array.meta_lol); //
	 * ResultsContents results = new ResultsContents(R.raw.bene_bgm,
	 * metadata[2],resizedBmp); ResultsContents results = new ResultsContents(0,
	 * metadata[2], resizedBmp); return results; }
	 */

	private ResultsContents generateHuman(Bitmap originBmp,
			RequestContents request, int original_width, int original_height,
			boolean isLandScape, boolean isLargeImage) {

		float resizeRatio = 1.0f;
		Bitmap bitmap_canvas = null;

		if (isLargeImage) {
			resizeRatio = isLandScape ? TARGET_LANDSCAPE_WIDTH / original_width
					: TARGET_PORTRAIT_WIDTH / original_width;
			original_width *= resizeRatio;
			original_height *= resizeRatio;
			bitmap_canvas = Bitmap.createScaledBitmap(originBmp,
					original_width, original_height, true);
		} else {
			resizeRatio = isLandScape ? original_width / TARGET_LANDSCAPE_WIDTH
					: original_width / TARGET_PORTRAIT_WIDTH;
			bitmap_canvas = originBmp.copy(Config.RGB_565, true);
		}

		final int canvasWidth = bitmap_canvas.getWidth();
		final int canvasHeight = bitmap_canvas.getHeight();

		Canvas c = new Canvas(bitmap_canvas);

		// 1. logo, date drawing
		Options opts = new Options();
		opts.inDensity = bitmap_canvas.getDensity();
		opts.inPreferredConfig = Config.RGB_565;

		Paint paint = new Paint();
		paint.setAntiAlias(true);

		Bitmap logo = BitmapFactory.decodeResource(res, R.drawable.human_logo,
				opts);
		Bitmap date = BitmapFactory.decodeResource(res, R.drawable.human_date,
				opts);

		if (isLargeImage) {
			c.drawBitmap(date, canvasWidth * 0.05f, canvasHeight * 0.06f, paint);
			c.drawBitmap(logo, canvasWidth * 0.85f, canvasHeight * 0.06f, paint);
			logo.recycle();
			date.recycle();
		} else {
			int logoWidth = (int) (logo.getWidth() * resizeRatio);
			int logoHeight = (int) (logo.getHeight() * resizeRatio);
			int dateWidth = (int) (date.getWidth() * resizeRatio);
			int dateHeight = (int) (date.getHeight() * resizeRatio);
			Bitmap bitmap_resize_logo = Bitmap.createScaledBitmap(logo,
					logoWidth, logoHeight, true);
			Bitmap bitmap_resize_date = Bitmap.createScaledBitmap(date,
					dateWidth, dateHeight, true);
			c.drawBitmap(bitmap_resize_date, canvasWidth * 0.05f,
					canvasHeight * 0.06f, paint);
			c.drawBitmap(bitmap_resize_logo, canvasWidth * 0.85f,
					canvasHeight * 0.06f, paint);
			bitmap_resize_date.recycle();
			bitmap_resize_logo.recycle();
		}

		// 2 text drawing
		float textSize = 50.0f;
		float strokeWidth = 9.0f;
		if (!isLargeImage) {
			textSize *= resizeRatio;
			strokeWidth *= resizeRatio;
		}

		Typeface fonts = Typeface.createFromAsset(res.getAssets(),
				"BareunBatangOTFPro-3.otf");
		Typeface bold = Typeface.create(fonts, Typeface.BOLD);

		paint.setTypeface(bold);
		paint.setTextScaleX(0.96f);

		// 2-1. textPaint, borderPaint
		Paint textPaint = new Paint(paint);
		textPaint.setTextSize(textSize);
		textPaint.setColor(Color.WHITE);
		textPaint.setStyle(Style.FILL);
		// textPaint.setShadowLayer(10.0f, 0.0f, 0.0f, Color.BLACK);

		Paint borderPaint = new Paint(paint);
		borderPaint.setTextSize(textSize);
		borderPaint.setStrokeWidth(strokeWidth);
		borderPaint.setStrokeCap(Cap.ROUND);
		borderPaint.setStrokeJoin(Join.ROUND);
		borderPaint.setColor(Color.BLACK);
		borderPaint.setStyle(Style.STROKE);

		String name = request.getArgs()[0];
		String age = request.getArgs()[1];
		String nick = request.getArgs()[2];
		String text = request.getArgs()[3];
		String pd = request.getArgs()[4];

		// color 171 163 82

		if (text.equals("")) {
			// only top name mode
			float nameTopPoint = canvasHeight * 0.85f;
			if (!nick.equals("")) {
				// String name = request.getHuman_name();
				String ageAndAlias = "(" + age + ")" + " / " + nick;
				float nameWidth = textPaint.measureText(name);
				textPaint.setTextSize(textSize * 0.80f);
				float ageWidth = textPaint.measureText(ageAndAlias);
				float fullTextWidth = nameWidth + ageWidth;
				textPaint.setTextSize(textSize);

				float startX = canvasWidth / 2 - fullTextWidth / 2;
				c.drawText(name, startX, nameTopPoint, borderPaint);
				c.drawText(name, startX, nameTopPoint, textPaint);
				startX += textPaint.measureText(name);
				textPaint.setTextSize(textSize * 0.80f);
				borderPaint.setTextSize(textSize * 0.80f);
				c.drawText(ageAndAlias, startX, nameTopPoint, borderPaint);
				c.drawText(ageAndAlias, startX, nameTopPoint, textPaint);
			} else {
				textPaint.setTextAlign(Align.RIGHT);
				borderPaint.setTextAlign(Align.RIGHT);

				c.drawText(name, canvasWidth / 2, nameTopPoint, borderPaint);
				c.drawText(name, canvasWidth / 2, nameTopPoint, textPaint);

				textPaint.setTextSize(textSize * 0.80f);
				borderPaint.setTextSize(textSize * 0.80f);
				textPaint.setTextAlign(Align.LEFT);
				borderPaint.setTextAlign(Align.LEFT);

				age = "(" + age + ")";
				c.drawText(age, canvasWidth / 2, nameTopPoint, borderPaint);
				c.drawText(age, canvasWidth / 2, nameTopPoint, textPaint);
			}
		} else if (name.equals("")) {
			// text mode
			text = "\"" + text + "\"";
			textPaint.setTextSize(textSize * 0.80f);
			borderPaint.setTextSize(textSize * 0.80f);
			textPaint.setTextAlign(Align.CENTER);
			borderPaint.setTextAlign(Align.CENTER);

			float textTopPoint = canvasHeight * 0.85f;

			float textWidth = textPaint.measureText(text);
			if (canvasWidth >= textWidth) {
				c.drawText(text, canvasWidth / 2, textTopPoint, borderPaint);
				c.drawText(text, canvasWidth / 2, textTopPoint, textPaint);
				Rect bounds = new Rect();
				textPaint.getTextBounds(text, 0, text.length(), bounds);
				textTopPoint -= bounds.height() * 1.2f;
			} else {
				textTopPoint = canvasHeight * 0.90f;
				int length = text.length();
				String text1 = text.substring(0, length / 2);
				String text2 = text.substring(length / 2);
				c.drawText(text2, canvasWidth / 2, textTopPoint, borderPaint);
				c.drawText(text2, canvasWidth / 2, textTopPoint, textPaint);
				Rect bounds = new Rect();
				textPaint.getTextBounds(text2, 0, text2.length(), bounds);
				textTopPoint -= bounds.height() * 1.2f;
				c.drawText(text1, canvasWidth / 2, textTopPoint, borderPaint);
				c.drawText(text1, canvasWidth / 2, textTopPoint, textPaint);
				textTopPoint -= bounds.height() * 1.2f;
			}

			if (!pd.equals("")) {
				pd = "\"" + pd + "\"";
				textPaint.setColor(Color.rgb(228, 235, 167));
				float pdWidth = textPaint.measureText(pd);
				if (canvasWidth >= pdWidth) {
					c.drawText(pd, canvasWidth / 2, textTopPoint, borderPaint);
					c.drawText(pd, canvasWidth / 2, textTopPoint, textPaint);
				} else {
					int length = pd.length();
					String text1 = pd.substring(0, length / 2);
					String text2 = pd.substring(length / 2);
					c.drawText(text2, canvasWidth / 2, textTopPoint,
							borderPaint);
					c.drawText(text2, canvasWidth / 2, textTopPoint, textPaint);
					Rect bounds = new Rect();
					textPaint.getTextBounds(text2, 0, text2.length(), bounds);
					textTopPoint -= bounds.height() * 1.2f;
					c.drawText(text1, canvasWidth / 2, textTopPoint,
							borderPaint);
					c.drawText(text1, canvasWidth / 2, textTopPoint, textPaint);
					textTopPoint -= bounds.height() * 1.2f;
				}
			}

		} else {
			// full mode

			// 1.text print
			text = "\"" + text + "\"";
			textPaint.setTextSize(textSize * 0.80f);
			borderPaint.setTextSize(textSize * 0.80f);
			textPaint.setTextAlign(Align.CENTER);
			borderPaint.setTextAlign(Align.CENTER);
			float textTopPoint = canvasHeight * 0.85f;
			float textLeftPoint = 0.0f;

			float textWidth = textPaint.measureText(text);
			if (canvasWidth >= textWidth) {
				c.drawText(text, canvasWidth / 2, textTopPoint, borderPaint);
				c.drawText(text, canvasWidth / 2, textTopPoint, textPaint);
				Rect bounds = new Rect();
				textPaint.getTextBounds(text, 0, text.length(), bounds);
				textTopPoint -= bounds.height() * 1.4f;
				textLeftPoint = canvasWidth / 2 - bounds.width() / 2;
			} else {
				textTopPoint = canvasHeight * 0.90f;
				int length = text.length();
				String text1 = text.substring(0, length / 2);
				String text2 = text.substring(length / 2);
				c.drawText(text2, canvasWidth / 2, textTopPoint, borderPaint);
				c.drawText(text2, canvasWidth / 2, textTopPoint, textPaint);
				Rect bounds = new Rect();
				textPaint.getTextBounds(text2, 0, text2.length(), bounds);
				textTopPoint -= bounds.height() * 1.2f;
				c.drawText(text1, canvasWidth / 2, textTopPoint, borderPaint);
				c.drawText(text1, canvasWidth / 2, textTopPoint, textPaint);
				textTopPoint -= bounds.height() * 1.4f;
				textPaint.getTextBounds(text1, 0, text1.length(), bounds);
				textLeftPoint -= canvasWidth / 2 - bounds.width() / 2;
			}

			if (!pd.equals("")) {
				pd = "\"" + pd + "\"";
				textPaint.setColor(Color.rgb(228, 235, 167));
				float pdWidth = textPaint.measureText(pd);
				if (canvasWidth >= pdWidth) {
					c.drawText(pd, canvasWidth / 2, textTopPoint, borderPaint);
					c.drawText(pd, canvasWidth / 2, textTopPoint, textPaint);
					Rect bounds = new Rect();
					textPaint.getTextBounds(pd, 0, pd.length(), bounds);
					textTopPoint -= bounds.height() * 1.4f;
				} else {
					int length = pd.length();
					String text1 = pd.substring(0, length / 2);
					String text2 = pd.substring(length / 2);
					c.drawText(text2, canvasWidth / 2, textTopPoint,
							borderPaint);
					c.drawText(text2, canvasWidth / 2, textTopPoint, textPaint);
					Rect bounds = new Rect();
					textPaint.getTextBounds(text2, 0, text2.length(), bounds);
					textTopPoint -= bounds.height() * 1.2f;
					c.drawText(text1, canvasWidth / 2, textTopPoint,
							borderPaint);
					c.drawText(text1, canvasWidth / 2, textTopPoint, textPaint);
					textTopPoint -= bounds.height() * 1.4f;
				}

				textPaint.setColor(Color.WHITE);
			}

			textPaint.setTextAlign(Align.LEFT);
			borderPaint.setTextAlign(Align.LEFT);

			if (textLeftPoint >= canvasWidth / 3) {
				textLeftPoint = canvasWidth / 3;
			} else if (textLeftPoint <= 0) {
				textLeftPoint = canvasWidth / 8;
			}

			// 2. name print
			if (!nick.equals("")) {
				String ageAndAlias = "(" + age + ")" + " / " + nick;
				textPaint.setTextSize(textSize);
				borderPaint.setTextSize(textSize);

				c.drawText(name, textLeftPoint, textTopPoint, borderPaint);
				c.drawText(name, textLeftPoint, textTopPoint, textPaint);
				textLeftPoint += textPaint.measureText(name);
				textPaint.setTextSize(textSize * 0.80f);
				borderPaint.setTextSize(textSize * 0.80f);
				c.drawText(ageAndAlias, textLeftPoint, textTopPoint,
						borderPaint);
				c.drawText(ageAndAlias, textLeftPoint, textTopPoint, textPaint);
			} else {
				textPaint.setTextAlign(Align.LEFT);
				borderPaint.setTextAlign(Align.LEFT);

				c.drawText(name, textLeftPoint, textTopPoint, borderPaint);
				c.drawText(name, textLeftPoint, textTopPoint, textPaint);

				textLeftPoint += textPaint.measureText(name);

				textPaint.setTextSize(textSize * 0.80f);
				borderPaint.setTextSize(textSize * 0.80f);

				age = "(" + age + ")";
				c.drawText(age, textLeftPoint, textTopPoint, borderPaint);
				c.drawText(age, textLeftPoint, textTopPoint, textPaint);
			}
		}

		String[] metadata = res.getStringArray(R.array.meta_human);

		// ResultsContents results = new ResultsContents(R.raw.human_bgm,
		// metadata[2],bitmap_canvas);
		ResultsContents result = new ResultsContents(0, metadata[2],
				bitmap_canvas);

		originBmp.recycle();
		date.recycle();
		logo.recycle();
		return result;
	}

	private ResultsContents generateBene(Bitmap originBmp, int original_width,
			int original_height, boolean isLandScape, boolean isLargeImage) {

		Bitmap frame, resizedOriginal, newFrame = null;
		float resizeRatio;

		BitmapFactory.Options frameOpts = new Options();
		frameOpts.inDensity = originBmp.getDensity();
		frameOpts.inPreferredConfig = Config.RGB_565;

		frame = isLandScape ? BitmapFactory.decodeResource(res,
				R.drawable.bene_frame_landscape, frameOpts) : BitmapFactory
				.decodeResource(res, R.drawable.bene_frame_portrait, frameOpts);

		if (isLargeImage) {
			resizeRatio = isLandScape ? TARGET_LANDSCAPE_WIDTH
					/ (float) original_width : TARGET_PORTRAIT_WIDTH
					/ (float) original_width;
			original_width *= resizeRatio;
			original_height *= resizeRatio;
			resizedOriginal = Bitmap.createScaledBitmap(originBmp,
					original_width, original_height, true);
			newFrame = frame;
		} else {
			int frame_width = frame.getWidth();
			int frame_height = frame.getHeight();
			resizeRatio = (float) original_width / (float) frame_width;
			frame_width *= resizeRatio;
			frame_height *= resizeRatio;
			newFrame = Bitmap.createScaledBitmap(frame, frame_width,
					frame_height, true);
			resizedOriginal = originBmp;
		}

		int proceedWidth = resizedOriginal.getWidth();
		int proceedHeight = resizedOriginal.getHeight();

		// TODO outOfMemory Exception....
		Bitmap modifyBackground = null;
		modifyBackground = Bitmap.createBitmap(proceedWidth, proceedHeight,
				Config.RGB_565);

		Canvas c = new Canvas(modifyBackground);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();

		cm.setSaturation(0);
		ColorMatrix sepiaMatrix = new ColorMatrix();
		sepiaMatrix.setScale(1f, .95f, .82f, 1.0f);
		cm.setConcat(sepiaMatrix, cm);

		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);

		// TODO throw If Recycled....
		c.drawBitmap(resizedOriginal, 0, 0, paint);

		paint.reset();

		c.drawBitmap(newFrame, 0, proceedHeight - newFrame.getHeight(), paint);

		newFrame.recycle();
		resizedOriginal.recycle();
		frame.recycle();
		originBmp.recycle();

		String[] metadata = res.getStringArray(ContentsData.BENE.getMeta_id());

		ResultsContents results = new ResultsContents(0, metadata[2],
				modifyBackground);
		return results;
	}

	/*
	private static final float OLLEH_BANNER_FONT_SIZE = 50.0f;

	private ResultsContents generateOLLEH(Bitmap originBmp,
			RequestContents request, int original_width, int original_height,
			boolean isLandScape, boolean isLargeImage) {

		Bitmap frame, resizedOriginal, newFrame = null;
		Bitmap original_logo, newLogo = null;
		float resizeRatio;

		BitmapFactory.Options frameOpts = new Options();
		frameOpts.inDensity = originBmp.getDensity();
		frameOpts.inPreferredConfig = Config.RGB_565;

		original_logo = BitmapFactory.decodeResource(res,
				R.drawable.oolleh_logo, frameOpts);

		frame = isLandScape ? BitmapFactory.decodeResource(res,
				R.drawable.olleh_landscape, frameOpts) : BitmapFactory
				.decodeResource(res, R.drawable.olleh_portrait, frameOpts);

		if (isLargeImage) {
			resizeRatio = isLandScape ? TARGET_LANDSCAPE_WIDTH
					/ (float) original_width : TARGET_PORTRAIT_WIDTH
					/ (float) original_width;
			original_width *= resizeRatio;
			original_height *= resizeRatio;
			resizedOriginal = Bitmap.createScaledBitmap(originBmp,
					original_width, original_height, true);
			newFrame = frame;
			newLogo = original_logo;
		} else {
			int frame_width = frame.getWidth();
			int frame_height = frame.getHeight();
			resizeRatio = (float) original_width / (float) frame_width;
			frame_width *= resizeRatio;
			frame_height *= resizeRatio;
			newFrame = Bitmap.createScaledBitmap(frame, frame_width,
					frame_height, true);
			resizedOriginal = originBmp;

			float logo_width = original_logo.getWidth() * resizeRatio;
			float logo_height = original_logo.getHeight() * resizeRatio;

			newLogo = Bitmap.createScaledBitmap(original_logo,
					(int) logo_width, (int) logo_height, true);
		}

		int proceedWidth = resizedOriginal.getWidth();
		int proceedHeight = resizedOriginal.getHeight();

		// TODO outOfMemory Exception....
		Bitmap modifyBackground = null;
		modifyBackground = Bitmap.createBitmap(proceedWidth, proceedHeight,
				Config.RGB_565);

		Canvas c = new Canvas(modifyBackground);
		Paint paint = new Paint();

		// TODO throw If Recycled....
		c.drawBitmap(resizedOriginal, 0, 0, paint);

		paint.reset();
		
		String really = request.getArgs()[1];
		
		if (really != null) {
			Bitmap tempImpact = null;
			
			if (isLandScape) {
				tempImpact = BitmapFactory.decodeResource(res,
						R.drawable.gray_impact, frameOpts);
			}
			else {
				tempImpact = BitmapFactory.decodeResource(res, R.drawable.gray_impact_port, frameOpts);
			}
			
			Bitmap impact = Bitmap.createScaledBitmap(tempImpact, proceedWidth,
					proceedHeight, true);
			c.drawBitmap(impact, 0.0f, 0.0f, paint);
			
			tempImpact.recycle();
			impact.recycle();
		}

		
		c.drawBitmap(newFrame, 0, proceedHeight - newFrame.getHeight(), paint);


		paint.setColor(Color.WHITE);

		Typeface fonts = Typeface.createFromAsset(res.getAssets(),
				"NanumGothicBold.otf");

		paint.setTypeface(fonts);
		paint.setTextAlign(Align.CENTER);
		paint.setAntiAlias(true);

		String text = request.getArgs()[0];

		if (isLargeImage) {

			paint.setTextSize(OLLEH_BANNER_FONT_SIZE);
			float height = proceedHeight - newFrame.getHeight() * 0.4f;

			if (isLandScape) {
				c.drawText(text, proceedWidth / 2f, height, paint);
			} else {
				c.drawText(text, proceedWidth / 2f, height, paint);
			}
		} else {

			paint.setTextSize(OLLEH_BANNER_FONT_SIZE * resizeRatio);

			float height = proceedHeight - newFrame.getHeight() * 0.4f;

			if (isLandScape) {
				c.drawText(text, proceedWidth / 2f, height, paint);
			} else {
				c.drawText(text, proceedWidth / 2f, height, paint);
			}
		}

		// logo drawing
		c.drawBitmap(newLogo, proceedWidth * 0.05f, proceedWidth * 0.05f, paint);

		// really drawing
		if (really != null) {
			paint.setTextAlign(Align.RIGHT);
			fonts = Typeface.createFromAsset(res.getAssets(),
					"BareunBatangOTFPro-3.otf");
			paint.setTypeface(fonts);

			float reallySize = isLargeImage ? OLLEH_BANNER_FONT_SIZE * 1.2f
					: paint.getTextSize() * 1.2f;
			paint.setTextSize(reallySize);
			c.drawText(really, proceedWidth * 0.95f, proceedWidth * 0.20f,
					paint);
		}

		newFrame.recycle();
		resizedOriginal.recycle();
		frame.recycle();
		originBmp.recycle();
		original_logo.recycle();
		newLogo.recycle();

		String[] metadata = res.getStringArray(ContentsData.OLLEH.getMeta_id());

//		ResultsContents results = new ResultsContents(R.raw.olleh_bgm,
//				metadata[2], modifyBackground);
		ResultsContents results = new ResultsContents(0,
				metadata[2], modifyBackground);
		return results;
	}
	*/

}
