package com.example.imageschecker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	public CustomImageList adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		adapter = new CustomImageList(this, new ArrayList<>());
		((ListView) findViewById(R.id.list_view)).setAdapter(adapter);

		DisplayImageActivity.context = this;

		new Thread(new GetImagesList(this)).start();
	}
}
