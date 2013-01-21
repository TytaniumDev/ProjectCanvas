package com.tywholland.projectcanvas;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private static final double MAX_PIXELS = 2048;

	private TouchImageView mImageView;
	private Button mButton;
	private Button mSaveButton;
	private Bitmap mImage;
	private Bitmap mCurrentBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImage = null;
		mCurrentBitmap = null;
		setContentView(R.layout.activity_main);
		mImageView = (TouchImageView) findViewById(R.id.imageView1);
		mButton = (Button) findViewById(R.id.button1);
		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent, 0);
			}
		});
		mSaveButton = (Button) findViewById(R.id.savebutton);
		mSaveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCurrentBitmap != null) {
					String filename = "canvas.png";
					if (saveImageToExternalStorage(filename)) {
						Toast.makeText(getApplicationContext(),
								"Image saved as " + filename,
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	public boolean saveImageToExternalStorage(String filename) {
		String fullPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/Pictures/Screenshots";
		try {
			File dir = new File(fullPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			OutputStream fOut = null;
			File file = new File(fullPath, filename);
			file.createNewFile();
			fOut = new FileOutputStream(file);

			// 100 means no compression, the lower you go, the stronger the
			// compression
			mCurrentBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();

			MediaStore.Images.Media.insertImage(getApplicationContext().getContentResolver(),
					file.getAbsolutePath(), file.getName(), file.getName());
			return true;
		} catch (Exception e) {
			Log.e("saveToExternalStorage()", e.getMessage());
			return false;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case 0:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				// file path of selected image
				String filePath = cursor.getString(columnIndex);
				cursor.close();
				// Convert file path into bitmap image using below line.
				mImage = BitmapFactory.decodeFile(filePath);
				// Scale image and preserve aspect ratio
				int width = mImage.getWidth();
				int height = mImage.getHeight();
				double widthscale = width > MAX_PIXELS ? MAX_PIXELS / width : 1;
				double heightscale = height > MAX_PIXELS ? MAX_PIXELS / height
						: 1;
				if (heightscale < 1 || widthscale < 1) {
					// Scaling needs to be done
					width = (int) (width * Math.min(widthscale, heightscale));
					height = (int) (height * Math.min(widthscale, heightscale));
				}
				mImage = Bitmap.createScaledBitmap(mImage, width, height, true);

				// put bitmapimage in your imageview
				mImageView.setImageBitmap(mImage);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO: Make this a thread
		if (mImage != null) {

			switch (item.getItemId()) {
			case R.id.greyscale:
				mCurrentBitmap = Filters.doGreyscale(mImage);
				mImageView.setImageBitmap(mCurrentBitmap);
				return true;
			case R.id.rgb:
				mCurrentBitmap = Filters.doRGB(mImage);
				mImageView.setImageBitmap(mCurrentBitmap);
				return true;
			case R.id.rgbmaxed:
				mCurrentBitmap = Filters.doRGBMaxed(mImage);
				mImageView.setImageBitmap(mCurrentBitmap);
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		return super.onOptionsItemSelected(item);
	}

}
