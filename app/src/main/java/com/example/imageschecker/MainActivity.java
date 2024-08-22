package com.example.imageschecker;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

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
