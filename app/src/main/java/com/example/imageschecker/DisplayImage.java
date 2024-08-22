package com.example.imageschecker;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayImage extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_image);

		((android.widget.ImageView) findViewById(R.id.img)).setImageBitmap(
				android.graphics.BitmapFactory.decodeFile(getIntent().getStringExtra("imagePath"))
		);
	}
}
