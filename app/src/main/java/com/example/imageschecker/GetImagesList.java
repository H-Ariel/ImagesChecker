package com.example.imageschecker;

import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

// this class scan whatsapp images folder and show special images
class GetImagesList implements Runnable {

	// path of whatsapp images folder
	static final String root = "/storage/emulated/0/Android/media/com.whatsapp/WhatsApp/Media/WhatsApp Images/";

	ArrayList<String> specialImagesPaths;
	ArrayList<String> imagesPathsList;
	AppCompatActivity context;

	static boolean hasInstance = false; // make this class singleton

	public GetImagesList(MainActivity context) {
		// make only one instance of this class
		if (hasInstance) return;
		hasInstance = true;

		this.context = context;
		specialImagesPaths = new ArrayList<>();
		imagesPathsList = new ArrayList<>();

		File waDir = new File(root); // whatsapp directory
		if (!waDir.exists()) return;
		File[] filesList = waDir.listFiles();
		if (filesList == null) return;

		// sort by date in reversed order
		Arrays.sort(filesList, Comparator.comparingLong(File::lastModified).reversed());

		// save only images
		for (File file : filesList) {
			if (isImage(file)) {
				imagesPathsList.add(root + file.getName()); // save full path
			}
		}
	}

	@Override
	protected void finalize() {
		hasInstance = false;
	}

	@Override
	public void run() {
		String txt = "scan %d of " + imagesPathsList.size() + " images";
		int i = 1;

		for (String path : imagesPathsList) {
			// add message of percent of images that scanned
			((TextView) context.findViewById(R.id.tvPercent)).setText(String.format(txt, i++));

			// check if image is special
			if (ImageChecker.isSpecialImage(path)) {
				specialImagesPaths.add(0, path); // add to the top of the list

				// check if `context` is the current activity
				if (context == context.getApplicationContext()) {
					return;
				}

				context.runOnUiThread(() -> ((ListView) context.findViewById(R.id.list_view)).setAdapter(
						new CustomImageList(context, specialImagesPaths)
				));
			}
		}

		txt = "finish, " + specialImagesPaths.size() + " special images found";
		((TextView) context.findViewById(R.id.tvPercent)).setText(txt);
	}

	static boolean isImage(File file) {
		if (file == null) return false;
		if (!file.isFile()) return false;
		String fileName = file.getName();
		return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
	}
}
