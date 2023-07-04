package com.progetto.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
            String parametro = intent.getStringExtra("nomeChat");
            TextView title = findViewById(R.id.title);
            title.setText(parametro);

        UserSingleton userSingleton = UserSingleton.getInstance();

        ArrayList<String> stringList = userSingleton.getMessages(userSingleton.getRoomId(parametro));


        LinearLayout btnContainer = findViewById(R.id.btnContainer);

            for (String text : stringList) {
                if(!text.isEmpty()){
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

                if(!messaggio.isEmpty()){
                    messaggioInput.setText("");
                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                    String risposta = userSingleton.sendMessages(userSingleton.getId(userSingleton.getUsername()), userSingleton.getRoomId(parametro), messaggio);

                    if(risposta.equals("ok")){
                        TextView button = new TextView(getApplicationContext());

                        button.setText(userSingleton.getUsername()+": "+messaggio);
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

                        stringList.add(userSingleton.getUsername()+": "+messaggio);
                        btnContainer.addView(button);
                    }else{
                        Toast.makeText(getApplicationContext(), "C'è stato un problema", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    messaggioInput.setText("");
                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    Toast.makeText(getApplicationContext(), "Scrivi un messaggio!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}