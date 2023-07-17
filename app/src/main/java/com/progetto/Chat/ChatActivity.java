package com.progetto.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private UserSingleton userSingleton = UserSingleton.getInstance();
    private Handler handler;
    private ArrayList<String> stringList;
    private LinearLayout btnContainer;
    private String parametro;
    private Integer room_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TextView title = findViewById(R.id.title);
        Intent intent = getIntent();
        parametro = intent.getStringExtra("nomeChat");
        title.setText(parametro);

        room_id = userSingleton.getRoomId(parametro);

        stringList = userSingleton.getMessages(room_id);
        btnContainer = findViewById(R.id.btnContainer);

        populateMessages();

        Button btn_back = findViewById(R.id.btnBack);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChatActivity.this, StanzeActivity.class));
            }
        });

        Button btn_add = findViewById(R.id.btnAdd);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText messaggioInput = findViewById(R.id.txtinptMessage);
                String messaggio = messaggioInput.getText().toString();

                if (!messaggio.isEmpty()) {
                    messaggioInput.setText("");
                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    String username = userSingleton.getUsername();
                    Integer userId = userSingleton.getId(username);
                    String risposta = userSingleton.sendMessages(userId, room_id, messaggio);

                    if (risposta.equals("ok")) {
                        TextView button = new TextView(getApplicationContext());

                        button.setText(username + ": " + messaggio);
                        button.setBackgroundResource(R.drawable.button_corner_radius_blue);
                        button.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                        button.setTextSize(30);

                        int paddingHorizontal = 25;
                        int paddingVertical = 30;
                        button.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(40, 35, 40, 0);
                        button.setLayoutParams(params);

                        stringList.add(username + ": " + messaggio);
                        btnContainer.addView(button);
                    } else {
                        Toast.makeText(getApplicationContext(), "C'è stato un problema", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    messaggioInput.setText("");
                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Toast.makeText(getApplicationContext(), "Scrivi un messaggio!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Initialize the Handler
        handler = new Handler(Looper.getMainLooper());

        // Schedule the first update after 7 seconds
        handler.postDelayed(refreshRunnable, 7000);
    }

    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            // Update the messages
            stringList = userSingleton.getMessages(room_id);
            populateMessages();

            // Schedule the next update after 7 seconds
            handler.postDelayed(this, 7000);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // Restart the message update when the activity is resumed
        handler.post(refreshRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop the message update when the activity is paused
        handler.removeCallbacks(refreshRunnable);
    }

    private void populateMessages() {
        btnContainer.removeAllViews();

        for (String text : stringList) {
            if (!text.isEmpty()) {
                TextView button = new TextView(this);
                button.setText(text);
                button.setBackgroundResource(R.drawable.button_corner_radius_blue);
                button.setTextColor(ContextCompat.getColor(this, R.color.black));
                button.setTextSize(30);

                int paddingHorizontal = 25;
                int paddingVertical = 30;
                button.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(40, 35, 40, 0);
                button.setLayoutParams(params);

                btnContainer.addView(button);
            }
        }
    }
}
