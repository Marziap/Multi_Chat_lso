package com.progetto.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserSingleton userSingleton = UserSingleton.getInstance();

        Button btn_login = findViewById(R.id.btnAccedi);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input_username = findViewById(R.id.txtinptUsername);
                String username = input_username.getText().toString();
                EditText input_password = findViewById(R.id.txtinptPassword);
                String password = input_password.getText().toString();
                User user = new User(username, password);
                userSingleton.setUser(user);
                String loginResult = userSingleton.login(user);

                if(loginResult.equals("ok")){
                    Intent intent = new Intent(MainActivity.this, StanzeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Credenziali sbagliate", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btn_registra = findViewById(R.id.btnRegistrati);
        btn_registra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input_username = findViewById(R.id.txtinptUsername);
                String username = input_username.getText().toString();
                EditText input_password = findViewById(R.id.txtinptPassword);
                String password = input_password.getText().toString();

                User user = new User(username, password);
                userSingleton.setUser(user);
                String registraResult = userSingleton.registra(user);

                if(registraResult.equals("ok")){
                    Toast.makeText(getApplicationContext(), "Registrazione ok! Fai Accesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "C'è stato un problema", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}