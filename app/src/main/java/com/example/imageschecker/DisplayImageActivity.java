package com.example.imageschecker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

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
		String imagePath = getIntent().getStringExtra("imagePath");
		// delete image
		java.io.File file = new java.io.File(imagePath);
		if (file.delete()) {
			// remove image from list
			context.adapter.remove(imagePath);
			finish();
		}
	}
}
