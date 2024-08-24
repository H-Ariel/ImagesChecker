package com.example.imageschecker;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
	public CustomImageList adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		adapter = new CustomImageList(this, new ArrayList<>());
		((GridView) findViewById(R.id.grid_view)).setAdapter(adapter);
		DisplayImageActivity.context = this;

		new Thread(new GetImagesList(this)).start();
	}
}
