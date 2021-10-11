package com.tuti.apparis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AgregarProductoActivity extends AppCompatActivity {
    private ImageView imagenpro;
    private EditText nombrep,codigo,costo,precio,stock;
    private Button agregarproducto;
    private static final  int Gallery_Pick=1;
    private Uri imaagenUri;
    private String productoRandomkey,dowloandUri;
    private StorageReference ProductoImagenRef;
    private DatabaseReference ProductoRef;
    private ProgressDialog dialog;
    private String Categoria,Nom,Cod,Precio,Costo,Stock,CurrentDate,CurrentTime;

    private TextView mensajeCategoria;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Categoria= getIntent().getExtras().get("categoria").toString();
        ProductoImagenRef= FirebaseStorage.getInstance().getReference().child("Imagenes Producto");
        ProductoRef= FirebaseDatabase.getInstance().getReference().child("Productos");


        setContentView(R.layout.activity_agregar_producto);
        mensajeCategoria=findViewById(R.id.textox);
        nombrep=findViewById(R.id.nombrepro);
        codigo=findViewById(R.id.codigopro);
        costo=findViewById(R.id.costopro);
        precio=findViewById(R.id.preciopro);
        stock=findViewById(R.id.stock);
        agregarproducto=findViewById(R.id.agregar_producto);
        imagenpro=findViewById(R.id.imagenproducto);
        dialog=new ProgressDialog(this);


        mensajeCategoria.setText(Categoria+"\n Agregar Producto");

        imagenpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirGaleria();
            }
        });
        agregarproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarProducto();
            }
        });
    }



    private void AbrirGaleria() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/");
        startActivityForResult(intent,Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_Pick && resultCode==RESULT_OK && data != null){
            imaagenUri=data.getData();

            imagenpro.setImageURI(imaagenUri);

        }
    }
    private void ValidarProducto() {
        Nom=nombrep.getText().toString();
        Cod=codigo.getText().toString();
        Costo=costo.getText().toString();
        Precio=precio.getText().toString();
        Stock=stock.getText().toString();

        if (imaagenUri==null){
            Toast.makeText(this, "Primero agrega una imagen", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Nom)){
            Toast.makeText(this, "Ingresa el nombre del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Cod)){
            Toast.makeText(this, "Ingresa el codigo del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Precio)){
            Toast.makeText(this, "Ingresa el precio del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Costo)){
            Toast.makeText(this, "Ingresa el costo del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Stock)){
            Toast.makeText(this, "Ingresa el stock", Toast.LENGTH_SHORT).show();
        }else{
            GuardarInformacionProducto();
        }
    }

    private void GuardarInformacionProducto() {
        dialog.setTitle("Guardando Producto");
        dialog.setMessage("Espera mientras guardamos tu producto");
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat curreDateFormat=new SimpleDateFormat("MM-dd-yyyy");
        CurrentDate=curreDateFormat.format(calendar.getTime());

        SimpleDateFormat CurrentTimeFormat = new SimpleDateFormat("HH-mm-ss");
        CurrentTime = CurrentTimeFormat.format(calendar.getTime());

        productoRandomkey=CurrentDate+CurrentTime;
        final StorageReference filePath = ProductoImagenRef.child(imaagenUri.getLastPathSegment()+productoRandomkey+".jpg");
        final UploadTask uploadTask = filePath.putFile(imaagenUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String mensaje = e.toString();
                Toast.makeText(AgregarProductoActivity.this, "Error"+mensaje, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AgregarProductoActivity.this, "Imagen guardada exitosamente", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        dowloandUri=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            dowloandUri=task.getResult().toString();
                            Toast.makeText(AgregarProductoActivity.this, "Imagen Guardada en Firebase", Toast.LENGTH_SHORT).show();
                            GuardarenFirebase();
                        }else{
                            Toast.makeText(AgregarProductoActivity.this, "Error.....", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void GuardarenFirebase() {
        HashMap<String,Object> map=new HashMap<>();
        map.put("pid",productoRandomkey);
        map.put("fecha",CurrentDate);
        map.put("hora",CurrentTime);
        map.put("nombrep",Nom);
        map.put("codigo",Cod);
        map.put("precio",Precio);
        map.put("costo",Costo);
        map.put("stock",Stock);
        map.put("imagen",dowloandUri);
        map.put("categoria",Categoria);

        ProductoRef.child(productoRandomkey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(AgregarProductoActivity.this, AdminActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                    Toast.makeText(AgregarProductoActivity.this, "Solicitud Exitosa", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    String mensaje =task.getException().toString();
                    Toast.makeText(AgregarProductoActivity.this, "Error"+mensaje, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}