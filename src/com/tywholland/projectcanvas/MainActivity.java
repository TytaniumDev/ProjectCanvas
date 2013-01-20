package com.tywholland.projectcanvas;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private TouchImageView mImageView;
	private Button mButton;
	private Bitmap mImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImage = null;
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
				mImage = Bitmap.createScaledBitmap(mImage,
						Math.min(mImage.getWidth(), 2048),
						Math.min(mImage.getHeight(), 2048), true);

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
				mImageView.setImageBitmap(Filters.doGreyscale(mImage));
				return true;
			default:
				return super.onOptionsItemSelected(item);
			}
		}
		return super.onOptionsItemSelected(item);
	}

}
