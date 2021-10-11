package com.tuti.apparis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupAdminActivity extends AppCompatActivity {

    private EditText nombre,cedula,ciudad,telefono,direccion,correo;
    private Button guardar;
    private String phone;
    private CircleImageView imagen;
    private FirebaseAuth auth;
    private DatabaseReference UserRef;
    private ProgressDialog dialog;
    private String CurrentUserId;
    private static int Galery_Pick=1;
    private StorageReference UserImagenPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_admin);

        auth=FirebaseAuth.getInstance();
        CurrentUserId=auth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Admin");
        dialog=new ProgressDialog(this);
        UserImagenPerfil= FirebaseStorage.getInstance().getReference().child("Perfil");

        nombre=findViewById(R.id.admin_setup_nombre);
        cedula=findViewById(R.id.admin_setup_cedula);
        telefono=findViewById(R.id.admin_setup_telefono);
        ciudad=findViewById(R.id.admin_setup_ciudad);
        direccion=findViewById(R.id.admin_setup_direccion);
        correo=findViewById(R.id.admin_setup_correo);

        guardar=findViewById(R.id.admin_setup_guardar);
        imagen=(CircleImageView) findViewById(R.id.setup_imagen);

        //tomamos el numero de telefono
        Bundle bundle=getIntent().getExtras();
        if (bundle != null){
            correo.setText(bundle.getString("correo"));
        }

        guardar.setOnClickListener(v -> {
            GuardarInformacion();
        });

    }

    private void GuardarInformacion() {
        String nombres=nombre.getText().toString().toUpperCase();
        String cedulas=cedula.getText().toString();
        String phones=telefono.getText().toString();
        String ciudads=ciudad.getText().toString();
        String direccions=direccion.getText().toString();
        String correos=correo.getText().toString();

        if (TextUtils.isEmpty(nombres)){
            Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(cedulas) || cedulas.length()<10){
            Toast.makeText(this, "Cedula incorrecata", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(direccions)){
            Toast.makeText(this, "Ingrese direccion", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(ciudads)){
            Toast.makeText(this, "Ingrese ciudad", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phones)){
            Toast.makeText(this, "Ingrese celular", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(correos)){
            Toast.makeText(this, "Ingrese correo", Toast.LENGTH_SHORT).show();
        }else {
            dialog.setTitle("Guardado");
            dialog.setMessage("Por favor espere");
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);
            HashMap map=new HashMap();
            map.put("nombre",nombres);
            map.put("cedula",cedulas);
            map.put("direccion",direccions);
            map.put("ciudad",ciudads);
            map.put("telefono",phones);
            map.put("correo",correos);
            map.put("uid",CurrentUserId);

            UserRef.child(CurrentUserId).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        EnviaralInicio();
                    }else{
                        String mensajee =task.getException().toString();
                        Toast.makeText(SetupAdminActivity.this, "Error"+mensajee, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    private void EnviaralInicio() {
        Intent intent = new Intent(SetupAdminActivity.this,AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}