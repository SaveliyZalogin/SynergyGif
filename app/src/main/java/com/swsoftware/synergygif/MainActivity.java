package com.swsoftware.synergygif;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView videoView = findViewById(R.id.videoView);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String gifUrl = ApiHelper.getGifUrl();
                Log.d("url", gifUrl);
                runOnUiThread(new Runnable() {
                    @SuppressLint("SetJavaScriptEnabled")
                    @Override
                    public void run() {
//                        webView.setBackgroundColor(Color.TRANSPARENT);
//                        webView.getSettings().setLoadWithOverviewMode(true);
//                        webView.getSettings().setUseWideViewPort(true);
//                        webView.getSettings().setJavaScriptEnabled(true);
//                        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//                        webView.loadUrl(gifUrl);
//                        webView.setVisibility(View.VISIBLE);
                        videoView.setVideoPath(gifUrl);
                        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                        });
                        videoView.setVisibility(View.VISIBLE);
                        videoView.start();
                    }
                });
            }
        });
        thread.start();
    }
}