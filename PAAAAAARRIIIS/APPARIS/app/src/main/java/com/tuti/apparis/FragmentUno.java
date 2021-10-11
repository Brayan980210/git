package com.tuti.apparis;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentUno extends Fragment {
    private View fragmento;
    private ImageView food,drink,cake,alcohol;

    public FragmentUno() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmento= inflater.inflate(R.layout.fragment_uno, container, false);

        food=fragmento.findViewById(R.id.comida);
        drink=fragmento.findViewById(R.id.refrescos);
        cake=fragmento.findViewById(R.id.postres);
        alcohol=fragmento.findViewById(R.id.alcohol);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","comida");
                startActivity(intent);
            }
        });
        drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","refresco");
                startActivity(intent);
            }
        });
        cake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","postres");
                startActivity(intent);
            }
        });
        alcohol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),AgregarProductoActivity.class);
                intent.putExtra("categoria","alcohol");
                startActivity(intent);
            }
        });

        return fragmento;
    }

}