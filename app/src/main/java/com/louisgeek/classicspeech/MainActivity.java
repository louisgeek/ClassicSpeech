package com.louisgeek.classicspeech;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.louisgeek.speech.SpeechSynthesizerFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        findViewById(R.id.id_xxx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpeechSynthesizerFactory.getInstance().speak("das测试大大大萨达大萨达");
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpeechSynthesizerFactory.getInstance().release();
    }
}
