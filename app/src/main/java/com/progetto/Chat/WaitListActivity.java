package com.progetto.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class WaitListActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitlist);

        UserSingleton userSingleton = UserSingleton.getInstance();
        ArrayList<String> stringList = userSingleton.getWaitList(userSingleton.getId(userSingleton.getUsername()));


        LinearLayout parentLayout = findViewById(R.id.container);

        for (int i = 0; i < stringList.size(); i++) {
            String el = stringList.get(i);

            if(!el.isEmpty()){
                LayoutInflater inflater = LayoutInflater.from(this);
                View customComponentView = inflater.inflate(R.layout.richieste_component, parentLayout, false);

                TextView nomeEstanzaTextView = customComponentView.findViewById(R.id.nomeEstanza);
                nomeEstanzaTextView.setText(el);
                parentLayout.addView(customComponentView);

                Button btn_accetta = customComponentView.findViewById(R.id.btnAccept);
                Button btn_rifiuta = customComponentView.findViewById(R.id.btnRefuse);

                btn_accetta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String modifiedStringa = el.replace(": ", "%");
                        String[] parts = modifiedStringa.split("%");

                        int user_id = userSingleton.getId(parts[0]);
                        int room_id = userSingleton.getRoomId(parts[1]);
                        //fai insert

                        String response = userSingleton.acceptRequest(user_id, room_id);

                        if(response.equals("ok")){
                            userSingleton.declineRequest(user_id, room_id);
                            stringList.remove(el);
                            parentLayout.removeView(customComponentView);
                        }
                    }
                });

                btn_rifiuta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //delete da db e arraylist
                        String modifiedStringa = el.replace(": ", "%");
                        String[] parts = modifiedStringa.split("%");

                        int user_id = userSingleton.getId(parts[0]);
                        int room_id = userSingleton.getRoomId(parts[1]);

                        userSingleton.declineRequest(user_id, room_id);
                        stringList.remove(el);
                        parentLayout.removeView(customComponentView);
                    }
                });
            }

        }


        Button btn_indietro = findViewById(R.id.btnIndietro);
        btn_indietro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WaitListActivity.this, StanzeActivity.class));
            }
        });


    }
}