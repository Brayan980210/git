package com.tuti.apparis;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentTres extends Fragment {

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

    private View fragmento;

    public FragmentTres() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmento=inflater.inflate(R.layout.fragment_tres, container, false);

        auth= FirebaseAuth.getInstance();
        CurrentUserId=auth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Admin");
        dialog=new ProgressDialog(getContext());
        UserImagenPerfil= FirebaseStorage.getInstance().getReference().child("Perfil");

        nombre=fragmento.findViewById(R.id.fragment_nombre);
        cedula=fragmento.findViewById(R.id.fragment_cedula);
        telefono=fragmento.findViewById(R.id.fragment_telefono);
        ciudad=fragmento.findViewById(R.id.fragment_ciudad);
        direccion=fragmento.findViewById(R.id.fragment_direccion);
        correo=fragmento.findViewById(R.id.fragment_correo);

        guardar=fragmento.findViewById(R.id.fragment_guardar);
        imagen=(CircleImageView) fragmento.findViewById(R.id.imgen_fragment);

        UserRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.hasChild("correo")){
                    String nombres=snapshot.child("nombre").getValue().toString();
                    String cedulas=snapshot.child("cedula").getValue().toString();
                    String direccions=snapshot.child("direccion").getValue().toString();
                    String ciudads=snapshot.child("ciudad").getValue().toString();
                    String telefonos=snapshot.child("telefono").getValue().toString();
                    String correos=snapshot.child("correo").getValue().toString();

                    nombre.setText(nombres);
                    cedula.setText(cedulas);
                    direccion.setText(direccions);
                    ciudad.setText(ciudads);
                    telefono.setText(telefonos);
                    correo.setText(correos);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //tomamos el numero de telefono


        guardar.setOnClickListener(v -> {
            GuardarInformacion();
        });

        return fragmento;
    }

    private void GuardarInformacion() {
        String nombres=nombre.getText().toString().toUpperCase();
        String cedulas=cedula.getText().toString();
        String phones=telefono.getText().toString();
        String ciudads=ciudad.getText().toString();
        String direccions=direccion.getText().toString();
        String correos=correo.getText().toString();

        if (TextUtils.isEmpty(nombres)){
            Toast.makeText(getContext(), "Ingrese nombre", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(cedulas) || cedulas.length()<10){
            Toast.makeText(getContext(), "Cedula incorrecata", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(direccions)){
            Toast.makeText(getContext(), "Ingrese direccion", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(ciudads)){
            Toast.makeText(getContext(), "Ingrese ciudad", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phones)){
            Toast.makeText(getContext(), "Ingrese celular", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(correos)){
            Toast.makeText(getContext(), "Ingrese correo", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Eroor"+mensajee, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
        }
    }
    private void EnviaralInicio() {
        Intent intent = new Intent(getContext(),PrincipalActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
