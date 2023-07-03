package com.progetto.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RichiestaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String parametro;
        Intent intent = getIntent();
        parametro = intent.getStringExtra("nomeChat");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_richiesta);

        UserSingleton user = UserSingleton.getInstance();


        Button btn_invia = findViewById(R.id.btnSndRequest);
        btn_invia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.sendRequest(user.getId(user.getUsername()), user.getRoomId(parametro));
                Toast.makeText(getApplicationContext(), "Richiesta inviata!", Toast.LENGTH_SHORT).show();
            }
        });


        Button btn_annulla = findViewById(R.id.btnAnnulla);
        btn_annulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RichiestaActivity.this, StanzeActivity.class));
            }
        });
    }

}