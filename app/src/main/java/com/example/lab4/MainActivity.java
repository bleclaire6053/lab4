package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";
    private Button startButton;
    private volatile boolean stopThread = false;
    private TextView textView;
    private int percent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        textView = findViewById(R.id.textView);
    }

    class ExampleRunnable implements Runnable {
        @Override
        public void run() {
            mockFileDownloader();
        }
    }

    public void mockFileDownloader() {
        runOnUiThread(new Runnable() {
            @Override
            public void  run() {
                startButton.setText("DOWNLOADING...");
            }
        });


        for (int downloadProgress = 0; downloadProgress <= 100; downloadProgress = downloadProgress + 10) {
            percent = downloadProgress;
            if (stopThread) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startButton.setText("Start");
                    }
                });
                return;
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Download Progress: " + percent + "%");
                }
            });
            Log.d(TAG, "Download Progress: " + downloadProgress + "%");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setText("Start");
            }
        });
    }
    public void startDownload(View view) {
       stopThread = false;
       ExampleRunnable runnable = new ExampleRunnable();
       new Thread(runnable).start();
    }

    public void stopDownload(View view) {
        stopThread = true;
    }
}