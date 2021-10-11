package com.tuti.apparis;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarritoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView carProductoNombre,carProductoCantidad,carProductoPrecio;
    public ItemClickListener itemClickListener;
    public CarritoViewHolder(@NonNull View itemView) {
        super(itemView);
        carProductoNombre=itemView.findViewById(R.id.car_productonombre);
        carProductoCantidad=itemView.findViewById(R.id.car_productocantidad);
        carProductoPrecio=itemView.findViewById(R.id.car_productoprecio);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
}
