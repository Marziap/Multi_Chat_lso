package com.progetto.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class StanzeActivity extends AppCompatActivity implements Serializable {

    UserSingleton userSingleton = UserSingleton.getInstance();
    Integer user_id = userSingleton.getId(userSingleton.getUsername());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stanze);

        ArrayList<String> risposta = userSingleton.getAllStanze();

        Collections.reverse(risposta);

        LinearLayout btnContainer = findViewById(R.id.btnContainer);

        for (String text : risposta) {

            if(!text.isEmpty()){
                Button button = new Button(this);
                button.setText(text);
                button.setBackgroundResource(R.drawable.button_corner_radius_blue);
                button.setTextColor(ContextCompat.getColor(this, R.color.black));
                button.setTextSize(30); // Set text size
                button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL); // Allinea il testo a sinistra
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                int widthInPixels = (int) getResources().getDisplayMetrics().density * 364;
                int heightInPixels = (int) getResources().getDisplayMetrics().density * 102;

                int paddingHorizontal = 25;
                int paddingVertical = 30;
                button.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);


                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthInPixels, heightInPixels);
                params.setMargins(40, 35, 40, 0);
                button.setLayoutParams(params);


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Integer room_id = userSingleton.getRoomId(text);
                        String isdentroRoom = userSingleton.isInRoom(user_id, room_id);

                        String parametro = text;

                        if (isdentroRoom.equals("free")){
                            Intent intent = new Intent(StanzeActivity.this, ChatActivity.class);
                            intent.putExtra("nomeChat", parametro);
                            startActivity(intent);
                        }else{
                            //se no apri invia richiesta
                            Intent intent = new Intent(StanzeActivity.this, RichiestaActivity.class);
                            intent.putExtra("nomeChat", parametro);
                            startActivity(intent);
                        }
                    }
                });

                btnContainer.addView(button);
            }

        }


        Button btn_logout = findViewById(R.id.btnLogout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StanzeActivity.this, MainActivity.class));
            }
        });

        Button btn_waitinglist = findViewById(R.id.btnWaitList);
        btn_waitinglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StanzeActivity.this, WaitListActivity.class));
            }
        });

        Button btn_add = findViewById(R.id.btnAdd);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StanzeActivity.this, CreaChatActivity.class));
            }
        });
    }
}