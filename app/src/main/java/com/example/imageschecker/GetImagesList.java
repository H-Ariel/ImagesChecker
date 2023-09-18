package com.example.imageschecker;

import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

// this class scan whatsapp images folder and show special images
class GetImagesList implements Runnable {

	// path of whatsapp images folder
	static final String ROOT = "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images/";

	ArrayList<String> imagesPathsList;
	MainActivity context;
	int i = 0; // number of images that scanned. its global because its used in run() method and we want save it

	public GetImagesList(MainActivity context) {
		this.context = context;
		this.imagesPathsList = new ArrayList<>();

		File waDir = new File(ROOT); // whatsapp directory
		if (!waDir.exists()) return;
		File[] filesList = waDir.listFiles();
		if (filesList == null) return;

		// sort by date in reversed order
		Arrays.sort(filesList, Comparator.comparingLong(File::lastModified).reversed());

		// save only images
//		int x = 0; // TODO: remove this line
		for (File file : filesList) {
			if (isImage(file)) {
				imagesPathsList.add(ROOT + file.getName()); // save full path
			}
//			if (x++ > 10) break; // TODO: remove this line
		}
	}

	@Override
	public void run() {
		String txt = "scan %d of " + imagesPathsList.size() + " images";

		for (; i < imagesPathsList.size(); i++) {
			// add message of percent of images that scanned
			((TextView) context.findViewById(R.id.tvPercent)).setText(String.format(txt, i + 1));

			String path = imagesPathsList.get(i);

			// check if image is already scanned
			if (ImageChecker.isSpecialImage(path)) {
				// add image to the top of the list
				context.runOnUiThread(() -> context.adapter.insert(path, 0));
			}
		}

		txt = "finish, " + context.adapter.getCount() + " special images found";
		((TextView) context.findViewById(R.id.tvPercent)).setText(txt);
	}

	static boolean isImage(File file) {
		if (file == null) return false;
		if (!file.isFile()) return false;
		String fileName = file.getName();
		return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
	}
}
