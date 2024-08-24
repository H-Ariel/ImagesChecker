package com.example.imageschecker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Arrays;

public class ImageChecker {
	int[] pixels;
	int[] colorCounts;
	final int N = 64; // downsampling factor for image reduction

	public ImageChecker(String path) {
		// get image pixels
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		int[] bmPixels = new int[bitmap.getWidth() * bitmap.getHeight()];
		bitmap.getPixels(bmPixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

		// reduce image size by N (so it checks faster)
		this.pixels = new int[bmPixels.length / N];
		for (int i = 0; i < this.pixels.length; i++)
			this.pixels[i] = bmPixels[i * N];

		// initialize color count array (assuming there are at most 256 * 256 * 256 colors, adjust if needed)
		colorCounts = new int[N * N * N];
	}

	public boolean isSpecialImage() {
		// Reset color counts
		Arrays.fill(colorCounts, 0);

		int r, g, b;
		int index;

		for (int pixel : pixels) {
			r = (pixel >> 16) & 0xff;
			g = (pixel >> 8) & 0xff;
			b = pixel & 0xff;

			// Reduce colors to combine similar colors
			r /= N;
			g /= N;
			b /= N;

			index = (r * N + g) * N + b;
			colorCounts[index]++;
		}

		int maxCount = 0;
		for (int count : colorCounts) {
			if (count > maxCount) {
				maxCount = count;
			}
		}

		return (float) maxCount / pixels.length > 0.6;
	}

	public static boolean isSpecialImage(String filename) {
		return new ImageChecker(filename).isSpecialImage();
	}
}
