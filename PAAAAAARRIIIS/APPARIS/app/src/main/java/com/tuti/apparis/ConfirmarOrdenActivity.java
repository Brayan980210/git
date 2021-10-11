package com.tuti.apparis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmarOrdenActivity extends AppCompatActivity {

    private EditText nombre,correo,direccion,telefono;
    private Button confirmar;
    private String totalPago="";
    private FirebaseAuth auth;
    private String CurrentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_orden);

        totalPago=getIntent().getStringExtra("Total");
        Toast.makeText(this, "Total a pagar: $ "+totalPago, Toast.LENGTH_SHORT).show();

        auth=FirebaseAuth.getInstance();
        CurrentUserID=auth.getCurrentUser().getUid();

        nombre=findViewById(R.id.final_nombre);
        correo=findViewById(R.id.final_correo);
        direccion=findViewById(R.id.final_direccion);
        telefono=findViewById(R.id.final_telefono);

        confirmar=findViewById(R.id.final_boton_confirmar);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarDatos();
            }
        });

    }

    private void VerificarDatos() {

        if (TextUtils.isEmpty(nombre.getText().toString())){
            Toast.makeText(this, "Por favor conpleta el nombre", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(correo.getText().toString())){
            Toast.makeText(this, "Por favor conpleta el correo", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(direccion.getText().toString())){
            Toast.makeText(this, "Por favor conpleta la direccion", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(telefono.getText().toString())){
            Toast.makeText(this, "Por favor conpleta el telefono", Toast.LENGTH_SHORT).show();
        }else{
            ConfirmarOrden();
        }
    }

    private void ConfirmarOrden() {
        final String CurrenTime,CurrentDate;

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("MM-dd-yyyy");
        CurrentDate=dateFormat.format(calendar.getTime());

        SimpleDateFormat dateFormat1=new SimpleDateFormat("HH:mm:ss");
        CurrenTime=dateFormat1.format(calendar.getTime());

        final DatabaseReference OrderRef= FirebaseDatabase.getInstance().getReference().child("Ordenes").child(CurrentUserID);

        HashMap<String,Object> map =new HashMap<>();
        map.put("total",totalPago);
        map.put("nombre",nombre.getText().toString());
        map.put("correo",correo.getText().toString());
        map.put("direccion",direccion.getText().toString());
        map.put("telefono",telefono.getText().toString());
        map.put("hora",CurrenTime);
        map.put("fecha",CurrentDate);
        map.put("estado","No enviado");

        OrderRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Carrito").child("Usuario Compra").child(CurrentUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ConfirmarOrdenActivity.this, "Tu orden Fue tomada con exito", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ConfirmarOrdenActivity.this,PrincipalActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

}