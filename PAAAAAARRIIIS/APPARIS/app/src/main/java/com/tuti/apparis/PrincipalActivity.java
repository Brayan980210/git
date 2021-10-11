package com.tuti.apparis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.tuti.apparis.Modal.Productos;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    private String CurrentUserID;
    private DatabaseReference UserRef, ProductosRef;
    private String Telefono="";
    private FloatingActionButton botonFlotante;
    private RecyclerView recyclerMenu;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            Telefono=bundle.getString("phone");
        }
        auth= FirebaseAuth.getInstance();
        CurrentUserID =auth.getCurrentUser().getUid();
        UserRef= FirebaseDatabase.getInstance().getReference().child("Usuario");
        ProductosRef= FirebaseDatabase.getInstance().getReference().child("Productos");
        recyclerMenu=findViewById(R.id.recycler_menu);
        recyclerMenu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerMenu.setLayoutManager(layoutManager);
        botonFlotante=findViewById(R.id.fab);

        botonFlotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrincipalActivity.this,CarritoActivity.class);
                startActivity(intent);
            }
        });




        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
          drawerLayout.addDrawerListener(toggle);
          toggle.syncState();
          NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
          navigationView.setNavigationItemSelectedListener(this);

        View headerView =navigationView.getHeaderView(0);
        TextView nombreheader=(TextView)headerView.findViewById(R.id.usuario_nombre_perfil);

        UserRef.child(CurrentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    nombreheader.setText(snapshot.child("nombre").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=auth.getCurrentUser();
        if (firebaseUser == null){
            EnviarAlLogin();
        }else{
            VerificarUsuarioExitosamente();
        }
        FirebaseRecyclerOptions<Productos> options = new FirebaseRecyclerOptions.Builder<Productos>()
                .setQuery(ProductosRef, Productos.class).build();
        FirebaseRecyclerAdapter<Productos,ProductoViewHolder> adapter=new FirebaseRecyclerAdapter<Productos, ProductoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductoViewHolder holder, int position, @NonNull Productos model) {
                holder.nombreroducto.setText(model.getNombrep());
                holder.codigo.setText(model.getCodigo());
                holder.precio.setText("Precio : "+model.getPrecio()+" $");
                holder.cantidad.setText(model.getStock());
                Picasso.get().load(model.getImagen()).into(holder.productoImagen);

                holder.productoImagen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PrincipalActivity.this,ProductoDetalleActivity.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.producto_item_layout,viewGroup,false);
                ProductoViewHolder holder= new ProductoViewHolder(view);

                return holder;
            }
        };

        recyclerMenu.setAdapter(adapter);
        adapter.startListening();






    }
//pasa con el bton atra no serrar la aplicacion solo serrar en menu
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout =(DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    private void VerificarUsuarioExitosamente() {
        final String CurrentUserId=auth.getCurrentUser().getUid();
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (!snapshot.hasChild(CurrentUserId)){
                EnviarAlSetup();
            }else{

            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_principal_drawer,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);

    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();

        if (id==R.id.nav_carrito){
            ActivityCarrito();
        } else if (id==R.id.nav_categorias){
            ActivityCategoria();
        }else if (id==R.id.nav_perfil){
            ActivityPerfil();
        }else if (id==R.id.nav_salir){
            auth.signOut();
            EnviarAlLogin();
        }
        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void ActivityPerfil() {
        Toast.makeText(this, "Perfil", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(PrincipalActivity.this,PerfilActivity.class);
        startActivity(intent);
    }

    private void ActivityCategoria() {
        Intent intent =new Intent(PrincipalActivity.this,MapaActivity.class);
        startActivity(intent);
    }

    private void ActivityCarrito() {

        Intent intent =new Intent(PrincipalActivity.this,CarritoActivity.class);
        Toast.makeText(this, "Carrito", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void EnviarAlSetup() {
        Intent intent=new Intent(PrincipalActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void EnviarAlLogin(){
        Intent intent=new Intent(PrincipalActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}