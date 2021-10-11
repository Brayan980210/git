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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LogAdminActivity extends AppCompatActivity {

    private EditText etUser;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnRegistrar;


    private EditText numero,codigo;
    private Button enviarnumero,enviarcodigo;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String VerificacionID;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_admin);
        etUser = (EditText) findViewById(R.id.aetUser);
        etPassword = (EditText) findViewById(R.id.aetPassword);
        btnLogin = (Button)findViewById(R.id.abtnLogin);
        btnRegistrar = (Button)findViewById(R.id.abtnRegistrar);

        auth= FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userE=etUser.getText().toString();
                String passE=etPassword.getText().toString();
                //Ahora validamos por si uno de los campos esta vacío
                if(TextUtils.isEmpty(userE)){
                    //por si falta correo
                    Toast.makeText(LogAdminActivity.this,"Inserte correo",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passE)){
                    //por si falta password
                    Toast.makeText(LogAdminActivity.this,"Inserte contraseña",Toast.LENGTH_SHORT).show();
                    return;
                }

                //Ahora usamos el Auth para que se logee una vez registrado
                auth.signInWithEmailAndPassword(userE,passE).
                        //Le pasamos la clase registro
                                addOnCompleteListener(LogAdminActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(!task.isSuccessful()){
                                    Toast.makeText(LogAdminActivity.this,"A ocurrido un error",Toast.LENGTH_SHORT).show();
                                    return;
                                }else{
                                    Intent intent =new Intent(LogAdminActivity.this,AdminActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }


                            }
                        });
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogAdminActivity.this,RegistarActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });



    }






    private void EnviarPrincipal() {
        Intent intent=new Intent(LogAdminActivity.this,AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}