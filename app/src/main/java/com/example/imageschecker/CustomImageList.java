package com.example.imageschecker;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

public class CustomImageList extends ArrayAdapter<String> {

	String[] imagesPaths;
	Activity context;

	public CustomImageList(Activity context, String[] imagesPaths) {
		super(context, R.layout.row_item, imagesPaths);
		this.context = context;
		this.imagesPaths = imagesPaths;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (convertView == null)
			row = context.getLayoutInflater().inflate(R.layout.row_item, null, true);
		((ImageView) row.findViewById(R.id.img)).setImageBitmap(BitmapFactory.decodeFile(imagesPaths[position]));
		return row;
	}
}
