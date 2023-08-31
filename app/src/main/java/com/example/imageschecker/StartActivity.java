package com.example.imageschecker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

	// Defining Permission codes. We can give any value but unique for each permission.
	private static final int MANAGE_EXTERNAL_STORAGE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		// ask user to allow storage permission
		checkPermission(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE, MANAGE_EXTERNAL_STORAGE);

		findViewById(R.id.start_button).setOnClickListener(v -> {
			Intent intent = new Intent(StartActivity.this, MainActivity.class);
			StartActivity.this.startActivity(intent);
			StartActivity.this.finish();
		});
	}

	void checkPermission(String permission, int requestCode) {
		if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
			// Requesting the permission
			ActivityCompat.requestPermissions(this, new String[] { permission }, requestCode);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == MANAGE_EXTERNAL_STORAGE) {
			if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(this, "manage storage permission denied", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
