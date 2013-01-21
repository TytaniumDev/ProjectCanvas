package com.tywholland.projectcanvas;

import android.graphics.Bitmap;
import android.graphics.Color;

public class Filters {
	public static Bitmap doGreyscale(Bitmap src) {
		// constant factors
		final double GS_RED = 0.299;
		final double GS_GREEN = 0.587;
		final double GS_BLUE = 0.114;

		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				src.getConfig());
		// pixel information
		int A, R, G, B;
		int pixel;

		// get image size
		int width = src.getWidth();
		int height = src.getHeight();

		// scan through every single pixel
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get one pixel color
				pixel = src.getPixel(x, y);
				// retrieve color of all channels
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				// take conversion up to one single value
				R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
				// set new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	public static Bitmap doRGB(Bitmap src) {
		// get image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// pixel information
		int A, R, G, B;
		int pixel;

		// scan through every single pixel
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get one pixel color
				pixel = src.getPixel(x, y);
				// retrieve color of all channels
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				// set new pixel color to highest value out of RGB, and only use
				// that color
				bmOut.setPixel(x, y, Color.argb(A, R > G && R > B ? R : 0, G > R && G > B ? G : 0, B > G && B > R ? B : 0));
			}
		}

		// return final image
		return bmOut;
	}
	
	public static Bitmap doRGBMaxed(Bitmap src) {
		// get image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// pixel information
		int A, R, G, B;
		int pixel;

		// scan through every single pixel
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				// get one pixel color
				pixel = src.getPixel(x, y);
				// retrieve color of all channels
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				// set new pixel color to highest value out of RGB, and only use
				// that color
				bmOut.setPixel(x, y, Color.argb(A, R > G && R > B ? 255 : 0, G > R && G > B ? 255 : 0, B > G && B > R ? 255 : 0));
			}
		}

		// return final image
		return bmOut;
	}
}
