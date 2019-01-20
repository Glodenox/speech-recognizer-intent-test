package com.tomputtemans.speechrecognizerintenttest;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        status = findViewById(R.id.textView);
        Button speech = findViewById(R.id.speech);
        speech.setOnClickListener(this);
        Button speechLocale = findViewById(R.id.speechLocale);
        speechLocale.setOnClickListener(this);
        TextView localeText = findViewById(R.id.localeText);
        localeText.setText("Your locale is " + Locale.getDefault().getLanguage());
        Button speechLanguage = findViewById(R.id.speechLanguage);
        speechLanguage.setOnClickListener(this);
        status.setText("Initialized");
    }

    @Override
    public void onClick(View view) {
        if (!checkAudioPermission()) {
            status.setText("Requesting permission");
            return;
        }
        Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        switch (view.getId()) {
            case R.id.speech:
                startActivityForResult(recognizerIntent, 1);
                status.setText("Started listening");
                break;
            case R.id.speechLocale:
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().getLanguage());
                startActivityForResult(recognizerIntent, 1);
                status.setText("Started listening with locale set (" + Locale.getDefault().getLanguage() + ")");
                break;
            case R.id.speechLanguage:
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
                startActivityForResult(recognizerIntent, 1);
                status.setText("Started listening with language set to en-US");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            status.setText("Cancelled");
            return;
        }
        List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        if (results != null && results.size() == 0) {
            status.setText("No results");
        } else {
            String resultString = "";
            for (String result : results) {
                resultString += "\n" + result;
            }
            status.setText("One or more results:" + resultString);
        }
    }

    private boolean checkAudioPermission() {
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                Toast.makeText(this, "Record audio is required", Toast.LENGTH_LONG).show();
            } else {
                requestPermissions( new String[]{ Manifest.permission.RECORD_AUDIO }, 1);
            }
        }
        return false;
    }
}
