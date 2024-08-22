package com.example.imageschecker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayImageActivity extends AppCompatActivity {

	static public MainActivity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_image);

		((android.widget.ImageView) findViewById(R.id.img)).setImageBitmap(
				android.graphics.BitmapFactory.decodeFile(getIntent().getStringExtra("imagePath"))
		);

		findViewById(R.id.btn).setOnClickListener(v -> deleteImage());
	}

	void deleteImage() {
		new AlertDialog.Builder(this)
				.setTitle("Confirmation")
				.setMessage("Are you sure?\nThis action cannot be undone.")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String imagePath = getIntent().getStringExtra("imagePath");
						if (imagePath != null) {
							// delete image
							java.io.File file = new java.io.File(imagePath);
							if (file.delete()) {
								// remove image from list
								context.adapter.remove(imagePath);
								finish();
							}
						}
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.show();
	}
}
