package com.tuti.apparis;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView codigo,nombreroducto,precio,cantidad;
    public ImageView productoImagen;
    public ItemClickListener listener;

    public ProductoViewHolder(@NonNull View itemView) {
        super(itemView);
        codigo=itemView.findViewById(R.id.i_codigoproducto);
        nombreroducto=itemView.findViewById(R.id.i_nombreproducto);
        precio=itemView.findViewById(R.id.i_precioproducto);
        cantidad=itemView.findViewById(R.id.i_cantidadproducto);
        productoImagen=itemView.findViewById(R.id.i_imagenproducto);
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v,getAdapterPosition(),false);
    }
}
