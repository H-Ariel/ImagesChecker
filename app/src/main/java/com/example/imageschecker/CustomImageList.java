package com.example.imageschecker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import java.util.ArrayList;

public class CustomImageList extends ArrayAdapter<String> {

	ArrayList<String> imagesPaths;
	Activity context;

	public CustomImageList(Activity context, ArrayList<String> imagesPaths) {
		super(context, R.layout.row_item, imagesPaths);
		this.context = context;
		this.imagesPaths = imagesPaths;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (convertView == null)
			row = context.getLayoutInflater().inflate(R.layout.row_item, null, true);
		((ImageView) row.findViewById(R.id.img)).setImageBitmap(BitmapFactory.decodeFile(imagesPaths.get(position)));

		// when click on image, open it in new activity
		row.setOnClickListener(v -> {
			Intent myIntent = new Intent(context, DisplayImage.class);
			myIntent.putExtra("imagePath", imagesPaths.get(position));
			context.startActivity(myIntent);
		});

		return row;
	}
}
