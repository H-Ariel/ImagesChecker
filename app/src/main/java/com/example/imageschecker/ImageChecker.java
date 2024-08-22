package com.example.imageschecker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.TreeMap;

// this class is used to check if an image is a special image (special image is an image that related to studies)
class ImageChecker {
	int[] pixels;

	public ImageChecker(String path) {
		// get image pixels
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		int[] bmPixels = new int[bitmap.getWidth() * bitmap.getHeight()];
		bitmap.getPixels(bmPixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

		// reduce image size by N (so it check faster)
		int N = 64;
		this.pixels = new int[bmPixels.length / N];
		for (int i = 0; i < this.pixels.length; i++)
			this.pixels[i] = bmPixels[i * N];
	}

	public boolean isSpecialImage() {
		// create colors dictionary
		TreeMap<Integer, Integer> colorsCount = new TreeMap<>();

		// iterate pixels
		for (int pixel : pixels) {
			int r = (pixel >> 16) & 0xff;
			int g = (pixel >> 8) & 0xff;
			int b = pixel & 0xff;

			// reduce colors so we can combine similar colors
			r /= 64;
			g /= 64;
			b /= 64;

			int color = (r << 16) + (g << 8) + b, count = 1;
			if (colorsCount.containsKey(color))
				count += colorsCount.get(color);
			colorsCount.put(color, count);
		}

		int commonPixelCount = colorsCount.values().stream().max(Integer::compare).get();
		return (float) commonPixelCount / pixels.length > 0.6;
	}

	public static boolean isSpecialImage(String filename) {
		return new ImageChecker(filename).isSpecialImage();
	}
}
