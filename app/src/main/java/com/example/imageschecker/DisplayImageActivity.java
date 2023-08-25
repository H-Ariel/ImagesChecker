package com.example.imageschecker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class DisplayImageActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_image);

		((android.widget.ImageView) findViewById(R.id.img)).setImageBitmap(
				android.graphics.BitmapFactory.decodeFile(getIntent().getStringExtra("imagePath"))
		);
	}
}
