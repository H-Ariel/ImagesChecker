package com.example.imageschecker;

import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

// this class scan whatsapp images folder and show special images
class GetImagesList implements Runnable {

	// path of whatsapp images folder
	static final String ROOT = "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images/";

	final List<String> imagesPathsList = new ArrayList<>();
	final MainActivity context;
	int i = 0; // number of images that scanned. its global because its used in run() method and we want save it

	public GetImagesList(MainActivity context) {
		this.context = context;

		File waDir = new File(ROOT); // whatsapp directory
		if (!waDir.exists()) return;
		File[] filesList = waDir.listFiles();
		if (filesList == null) return;

		// sort by date in reversed order
		Arrays.sort(filesList, Comparator.comparingLong(File::lastModified).reversed());

		// save only images
		for (File file : filesList) {
			if (isImage(file)) {
				imagesPathsList.add(ROOT + file.getName()); // save full path
			}
		}
	}

	static class PathManager {
		final List<String> imagesPathsList;

		final Object mutexNext = new Object();

		static public class Path {
			public String path = "";

			public Path(String path) {
				this.path = path;
			}
		}

		public PathManager(List<String> imagesPathsList) {
			this.imagesPathsList = imagesPathsList;
		}

		int i=-1;
		public Path getNext() {
			synchronized (mutexNext) {
				i+=1;
				if (i >= imagesPathsList.size())
					return new Path(imagesPathsList.get(i));
			}
			return null;
		}
	}

	@Override
	public void run() {
		final String txt = "scan %d of " + imagesPathsList.size() + " images";

		List<String> specialImages = new ArrayList<>();

		// scan and filter images:

		for (; i < imagesPathsList.size(); i++) {
			// add message of percent of images that scanned
			((TextView) context.findViewById(R.id.tvPercent)).setText(String.format(txt, i + 1));

			String path = imagesPathsList.get(i);

			// check if image is already scanned
			if (ImageChecker.isSpecialImage(path))
				specialImages.add(path);
		}

		// try scan faster using threads. It does not work now :/
		/*
		((TextView) context.findViewById(R.id.tvPercent)).setText(txt);

		PathManager pathManager = new PathManager(imagesPathsList);
		int threadsCount = 5;
		List<Thread> threads = new LinkedList<>();
		final Object pathMutex = new Object();
		while (threadsCount-- > 0)
			threads.add(new Thread(() -> {
				PathManager.Path path;
				while ((path = pathManager.getNext()) != null) {
					if (ImageChecker.isSpecialImage(path.path)) {
						synchronized (pathMutex) {
							specialImages.add(path.path);
						}
					}
				}
			}));
		for (Thread thread : threads) thread.start();
		try {
			for (Thread thread : threads) thread.join();
		} catch (InterruptedException ignore) {
		}
		*/

		for (String path : specialImages)
			context.runOnUiThread(() -> context.adapter.add(path));

		((TextView) context.findViewById(R.id.tvPercent)).setText("finish, " + context.adapter.getCount() + " special images found");
	}

	static boolean isImage(File file) {
		if (file == null) return false;
		if (!file.isFile()) return false;
		String fileName = file.getName();
		return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
	}
}
