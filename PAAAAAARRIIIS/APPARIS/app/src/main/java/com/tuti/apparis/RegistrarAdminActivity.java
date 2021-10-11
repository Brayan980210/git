package com.tuti.apparis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrarAdminActivity extends AppCompatActivity {

    private EditText etUser;
    private EditText etPassword;
    private Button btnRegistrar;
    FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_admin);

        progressDialog=new ProgressDialog(this);

        //Instanciamos igualmente
        auth=FirebaseAuth.getInstance();

        etUser=(EditText) findViewById(R.id.etUser);
        etPassword=(EditText) findViewById(R.id.etPassword);
        btnRegistrar=(Button) findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String userE=etUser.getText().toString();
                String passE=etPassword.getText().toString();
                //Ahora validamos por si uno de los campos esta vacío
                if(TextUtils.isEmpty(userE)){
                    //por si falta correo
                    Toast.makeText(RegistrarAdminActivity.this,"Inserte correo",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passE)){
                    //por si falta password
                    Toast.makeText(RegistrarAdminActivity.this,"Inserte contraseña",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("En proceso");
                progressDialog.show();

                //Ahora usamos el metodo
                auth.createUserWithEmailAndPassword(userE,passE).addOnCompleteListener(RegistrarAdminActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Task=Tareas devuelve si la tarea si se cumple
                        //En este caso si se cumplio
                        Toast.makeText(RegistrarAdminActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                        //Si no logra registrarse
                        if(!task.isSuccessful()){
                            Toast.makeText(RegistrarAdminActivity.this,"Usuario no se ha podido registrar",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Intent i = new Intent(RegistrarAdminActivity.this,SetupAdminActivity.class);
                        i.putExtra("correo",etUser.getText().toString());
                        startActivity(i);
                    }
                });







            }
        });
    }
}