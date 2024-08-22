package com.example.imageschecker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StartActivity extends AppCompatActivity {

	// Defining Permission codes. We can give any value but unique for each permission.
	private static final int MANAGE_EXTERNAL_STORAGE = 100;
	private static final int READ_EXTERNAL_STORAGE = 101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		checkPermissions();

		findViewById(R.id.start_button).setOnClickListener(v -> {
			StartActivity.this.startActivity(new Intent(StartActivity.this, MainActivity.class));
			StartActivity.this.finish();
		});
	}

	void checkPermissions() {
		boolean hasPermission = Environment.isExternalStorageManager();
		if (!hasPermission) {
			// open setting and user let permission to app
			startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION));
		}

		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this,
					new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
					READ_EXTERNAL_STORAGE);
		}
	}
}
