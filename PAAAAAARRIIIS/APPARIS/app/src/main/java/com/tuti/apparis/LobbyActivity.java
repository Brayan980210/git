package com.tuti.apparis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LobbyActivity extends AppCompatActivity {
    private Button btn_usuario,btn_administrador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        btn_usuario=findViewById(R.id.btn_usuario);
        btn_administrador=findViewById(R.id.btn_administrador);


        btn_usuario.setOnClickListener(v->{
            Intent intent=new Intent(LobbyActivity.this,LoginActivity.class);
            startActivity(intent);
        });

        btn_administrador.setOnClickListener(v -> {
            Intent intent=new Intent(LobbyActivity.this,LogAdminActivity.class);
            startActivity(intent);
        });
    }
}