package com.tuti.apparis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tuti.apparis.Modal.Carrito;

import java.text.DecimalFormat;

public class CarritoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button siguiente;
    private TextView TotalPrecio, mensaje;

    private double PrecioTotaID=0.0;
    private String CurrentUserId;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        recyclerView=findViewById(R.id.carrito_lista);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        siguiente=findViewById(R.id.siguiente_proceso);
        TotalPrecio=findViewById(R.id.precio_total);
        mensaje=findViewById(R.id.mensaje1);

        auth=FirebaseAuth.getInstance();

        CurrentUserId=auth.getCurrentUser().getUid();

        siguiente.setOnClickListener(v -> {
            Intent intent=new Intent(CarritoActivity.this,ConfirmarOrdenActivity.class);
            intent.putExtra("Total",String.valueOf(PrecioTotaID));
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        VerificarEstadOrden();


        final DatabaseReference CartListRef = FirebaseDatabase.getInstance().getReference().child("Carrito");

        FirebaseRecyclerOptions<Carrito> options = new FirebaseRecyclerOptions.Builder<Carrito>()
                .setQuery(CartListRef.child("Usuario Compra").child(CurrentUserId).child("Productos"),Carrito.class).build();

        FirebaseRecyclerAdapter<Carrito, CarritoViewHolder> adapter=new FirebaseRecyclerAdapter<Carrito, CarritoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CarritoViewHolder holder, int position, @NonNull Carrito model) {

                holder.carProductoNombre.setText(model.getNombre());
                holder.carProductoCantidad.setText(model.getCantidad());
                holder.carProductoPrecio.setText(model.getPrecio());

                double UnTipoPrecio=(Double.valueOf(model.getPrecio())*Integer.valueOf(model.getCantidad()));

                PrecioTotaID = PrecioTotaID + UnTipoPrecio;

                DecimalFormat df=new DecimalFormat("#.00");
                TotalPrecio.setText("Total " + String.valueOf(df.format(PrecioTotaID) ));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence options[]=new CharSequence[]{
                                "Editar",
                                "Eliminar"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarritoActivity.this);
                        builder.setTitle("Opciones de Producto");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                if (i==0){
                                    Intent intent =new Intent(CarritoActivity.this,ProductoDetalleActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    startActivity(intent);
                                }
                                if (i==1){
                                    CartListRef.child("Usuario Compra")
                                            .child(CurrentUserId)
                                            .child("Productos")
                                            .child(model.getPid()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(CarritoActivity.this, "Producto Elimminado", Toast.LENGTH_SHORT).show();
                                                        Intent intent =new Intent(CarritoActivity.this,PrincipalActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }
            @NonNull
            @Override
            public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item_layout,parent,false);
                CarritoViewHolder holder = new CarritoViewHolder(view);

                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    private void VerificarEstadOrden() {
    }
}