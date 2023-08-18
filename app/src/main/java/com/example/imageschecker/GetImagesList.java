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

	String[] specialImagesPaths;
	ArrayList<String> imagesPathsList;
	AppCompatActivity context;


	public GetImagesList(AppCompatActivity context) {
		this.context = context;
		specialImagesPaths = new String[0]; // empty array
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
	public void run() {
		String txt = "scan %d of " + imagesPathsList.size() + " images";
		int i = 1;

		for (String path : imagesPathsList) {
			// add message of percent of images that scanned
			((TextView) context.findViewById(R.id.tvPercent)).setText(String.format(txt, i++));

			// check if image is special
			if (ImageChecker.isSpecialImage(path)) {
				addPath(path);
				context.runOnUiThread(() -> ((ListView) context.findViewById(R.id.list_view)).setAdapter(
						new CustomImageList(context, specialImagesPaths)
				));
			}
		}

		txt = "finish, " + specialImagesPaths.length + " special images found";
		((TextView) context.findViewById(R.id.tvPercent)).setText(txt);
	}

	// add new path to the start of `specialImagesPaths`
	void addPath(String path) {
		String[] tmp = new String[specialImagesPaths.length + 1];
		tmp[0] = path;
		System.arraycopy(specialImagesPaths, 0, tmp, 1, specialImagesPaths.length);
		specialImagesPaths = tmp;
	}

	static boolean isImage(File file) {
		if (file == null) return false;
		if (!file.isFile()) return false;
		String fileName = file.getName();
		return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
	}
}
