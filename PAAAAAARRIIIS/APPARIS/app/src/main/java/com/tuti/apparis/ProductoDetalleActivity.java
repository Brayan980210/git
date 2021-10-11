package com.tuti.apparis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tuti.apparis.Modal.Productos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductoDetalleActivity extends AppCompatActivity {
    private Button agregarCarrito;
    private ElegantNumberButton numeroBoton;
    private ImageView productoImagen;
    private TextView ProductoPrecio,ProductoDescripcion,ProductoNombre;

    private String productoID="",estado="Normal",CurrentUserID;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalle);

        productoID=getIntent().getStringExtra("pid");
        agregarCarrito=findViewById(R.id.boton_siguiente_detalles);
        numeroBoton=findViewById(R.id.numero_boton);
        productoImagen=findViewById(R.id.producto_imagen_detalles);
        ProductoPrecio=findViewById(R.id.producto_precio_detalles);
        ProductoNombre=findViewById(R.id.producto_nombre_detalles);
        ProductoDescripcion=findViewById(R.id.producto_descripcion_detalles);
        ObtenerDatosProducto(productoID);
        auth=FirebaseAuth.getInstance();
        CurrentUserID=auth.getCurrentUser().getUid();
        agregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estado.equals("Pedido")||estado.equals("Enviado")){
                    Toast.makeText(ProductoDetalleActivity.this, "Espere a que el primer pedido finalice ...", Toast.LENGTH_SHORT).show();
                }else{
                    agregarAlaLista();
                }
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        VerificaEstadoORden();
    }



    private void agregarAlaLista() {
        String CurrentTime,CurrentDate;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat data=new SimpleDateFormat("MM-dd-yyyy");
        CurrentDate=data.format(calendar.getTime());

        SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss");
        CurrentTime=time.format(calendar.getTime());

        final DatabaseReference CartListRef= FirebaseDatabase.getInstance().getReference().child("Carrito");

        final HashMap<String,Object> map = new HashMap<>();
        map.put("pid",productoID);
        map.put("nombre",ProductoNombre.getText().toString());
        map.put("precio",ProductoPrecio.getText());
        map.put("fecha",CurrentDate);
        map.put("hora",CurrentTime);
        map.put("cantidad",numeroBoton.getNumber());
        map.put("descuento","");

        CartListRef.child("Usuario Compra").child(CurrentUserID).child("Productos").child(productoID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    CartListRef.child("Administracion").child(CurrentUserID).child("Productos").child(productoID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ProductoDetalleActivity.this, "Agregado..", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(ProductoDetalleActivity.this,PrincipalActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        });

    }

    private void ObtenerDatosProducto(String productoID) {
        DatabaseReference ProductoRef=FirebaseDatabase.getInstance().getReference().child("Productos");
        ProductoRef.child(productoID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Productos productos=snapshot.getValue(Productos.class);
                    ProductoNombre.setText(productos.getNombrep());
                    ProductoDescripcion.setText(productos.getCodigo());
                    ProductoPrecio.setText(productos.getPrecio());

                    Picasso.get().load(productos.getImagen()).into(productoImagen);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void VerificaEstadoORden() {
        DatabaseReference OrdenRef;
        OrdenRef=FirebaseDatabase.getInstance().getReference().child("Ordenes").child(CurrentUserID);
        OrdenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String envioStado = snapshot.child("estado").getValue().toString();
                    if (envioStado.equals("Enviado")){
                        estado="Enviado";
                    }else if (envioStado.equals("No enviado")){
                        estado="Pedido";

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}