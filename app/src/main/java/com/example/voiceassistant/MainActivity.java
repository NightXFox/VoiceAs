package com.example.voiceassistant;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    protected Button sendButton;
    protected EditText questionText;
    protected TextView chatWindow;
    protected TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendButton = findViewById(R.id.sendButton);
        questionText = findViewById(R.id.questionField);
        chatWindow = findViewById(R.id.chatWindow);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                onSend();
            }
        });

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i!= TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(new Locale("ru"));
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String[] allMessages = chatWindow.getText().toString().split("\n");
        outState.putStringArray("messageArray", allMessages);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String[] savedMessages = savedInstanceState.getStringArray("messageArray");
        for (String message : savedMessages) {
            chatWindow.append(message + "\n");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onSend() {
        String text = questionText.getText().toString();
        String answer = AI.getAnswer(text);
        chatWindow.append(">> " + text + "\n");
        chatWindow.append("<< " + answer + "\n");
        textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH,null, null );
        questionText.setText("");
    }
}