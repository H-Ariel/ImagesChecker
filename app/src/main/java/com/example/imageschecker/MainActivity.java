package com.example.imageschecker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!GetImagesList.hasInstance) {
            new Thread(new GetImagesList(this)).start();
        }
    }

    /*
    private boolean isInFront;
    public boolean isInFront() {
        return isInFront;
    }

    @Override
    public void onResume() {
        super.onResume();
        isInFront = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isInFront = false;
    }
    */
}
