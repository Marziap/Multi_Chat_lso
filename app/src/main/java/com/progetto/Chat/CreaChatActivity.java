package com.progetto.Chat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class CreaChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creachat);

        UserSingleton userSingleton = UserSingleton.getInstance();

        ArrayList<String> users = userSingleton.getUsers();
        users.remove(userSingleton.getUsername());

        ArrayList<String> selectedTexts = new ArrayList<>();

        LinearLayout btnContainer = findViewById(R.id.btnContainer);

        for (String text : users) {

            if(!text.isEmpty()){
                CheckBox button = new CheckBox(this);
                button.setText(text);
                button.setBackgroundResource(R.drawable.button_corner_radius_blue);
                button.setTextColor(ContextCompat.getColor(this, R.color.black));
                button.setTextSize(30); // Set text size
                button.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                int widthInPixels = (int) getResources().getDisplayMetrics().density * 364;
                int heightInPixels = (int) getResources().getDisplayMetrics().density * 102;

                int paddingHorizontal = 25;
                int paddingVertical = 45;
                button.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);


                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(widthInPixels, heightInPixels);
                params.setMargins(75, 55, 75, 0);
                button.setLayoutParams(params);


                button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            String text = button.getText().toString();
                            selectedTexts.add(text);
                        } else {
                            String text = button.getText().toString();
                            selectedTexts.remove(text);
                        }
                    }
                });


                btnContainer.addView(button);
            }

        }




        Button btn_annulla = findViewById(R.id.btnAnnulla);
        btn_annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreaChatActivity.this, StanzeActivity.class));
            }
        });

        Button btn_conferma = findViewById(R.id.btnConferma);
        btn_conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textInput = findViewById(R.id.txtinptNomeChat);
                String inputText = textInput.getText().toString();

                UserSingleton userSingleton = UserSingleton.getInstance(); // Get the UserSingleton instance

                Integer id = userSingleton.getId(userSingleton.getUsername()); // Pass the UserSingleton instance

                System.out.println("ID: " + id);

                String risposta = userSingleton.creaStanza(inputText, id);

                if(risposta.equals(inputText)){

                    Integer room_id = userSingleton.getRoomId(inputText);
                    System.out.println("room_id "+room_id);

                    for (String el : selectedTexts){
                        System.out.println("Selezionato "+el);
                        Integer user_id = userSingleton.getId(el);
                        System.out.println("user_id "+user_id);
                        userSingleton.addToStanza(user_id, room_id);
                    }
                    userSingleton.addToStanza(userSingleton.getId(userSingleton.getUsername()), room_id);
                }
                //insert in db
                startActivity(new Intent(CreaChatActivity.this, StanzeActivity.class));
            }
        });

    }
}
