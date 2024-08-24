package com.example.imageschecker;

import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// This class scans WhatsApp images folder and shows special images
class GetImagesList implements Runnable {
	// path of whatsapp images folder
	static final String ROOT = "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images/";

	final MainActivity context;
	final List<String> allImages = new ArrayList<>();
	int scannedImages = 0;

	public GetImagesList(MainActivity context) {
		this.context = context;

		File waDir = new File(ROOT); // whatsapp directory
		if (!waDir.exists()) return;
		File[] filesList = waDir.listFiles();
		if (filesList == null) return;

		// collect image paths sorted by last-modified date in descending order
		allImages.addAll(Arrays.stream(filesList)
				.sorted(Comparator.comparingLong(File::lastModified).reversed())
				.filter(GetImagesList::isImage)
				.map(file -> ROOT + file.getName())
				.collect(Collectors.toList()));
	}

	@Override
	public void run() {
		final int threadsCount = 5;
		final Thread[] threads = new Thread[threadsCount];
		final Object adapterMtx = new Object();

		final String txt = "Scan %d of " + allImages.size() + " images";

		for (int t = 0; t < threadsCount; t++) {
			threads[t] = new Thread(() -> {
				String path;
				while ((path = getNextPath()) != null) {
					if (ImageChecker.isSpecialImage(path))
						synchronized (adapterMtx) {
							// Update UI with the progress
							final int current = scannedImages;
							final String p = path;
							context.runOnUiThread(() -> {
								((TextView) context.findViewById(R.id.tvPercent)).setText(String.format(txt, current));
								context.adapter.add(p);
							});
						}

				}
			});
			threads[t].start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException ignore) {
			}
		}

		context.runOnUiThread(() -> ((TextView) context.findViewById(R.id.tvPercent))
				.setText("Finished, " + context.adapter.getCount() + " special images found"));
	}

	public String getNextPath() {
		synchronized (allImages) {
			if (scannedImages < allImages.size())
				return allImages.get(scannedImages++);
		}
		return null;
	}

	static boolean isImage(File file) {
		if (file == null) return false;
		if (!file.isFile()) return false;
		String filename = file.getName();
		return filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png");
	}
}
