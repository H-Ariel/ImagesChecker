package com.example.imageschecker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CustomImageList extends ArrayAdapter<String> {
	ArrayList<String> imagesPaths;
	Activity context; // TODO: MainActivity

	public CustomImageList(Activity context, ArrayList<String> imagesPaths) {
		super(context, R.layout.row_item, imagesPaths);
		this.context = context;
		this.imagesPaths = imagesPaths;
	}

	@Override
	public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
		ImageView imageView = (ImageView) convertView;
		if (imageView == null)
			imageView = (ImageView) context.getLayoutInflater().inflate(R.layout.row_item, parent, false);

		imageView.setImageBitmap(BitmapFactory.decodeFile(imagesPaths.get(position)));

		// When clicking on an image, open it in a new activity
		imageView.setOnClickListener(v -> {
			Intent myIntent = new Intent(context, DisplayImageActivity.class);
			myIntent.putExtra("imagePath", imagesPaths.get(position));
			context.startActivity(myIntent);
		});

		return imageView;
	}
}
