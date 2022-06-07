package com.example.pruebafotofinal.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.pruebafotofinal.modelo.plato;
import com.example.pruebafotofinal.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class platoAdapter extends RecyclerView.Adapter<platoAdapter.platoHolder> {
    List<plato> lista;
    int layout;
    Activity activity;

    public platoAdapter(List<plato> lista, int layout, Activity activity) {
        this.lista = lista;
        this.layout = layout;
        this.activity = activity;
    }

    @NonNull
    @Override
    public platoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout,parent,false);

        return new platoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull platoHolder holder, int position) {
        plato plato = lista.get(position);
        holder.txtid.setText(plato.getId());
        holder.txtnombre.setText(plato.getNombre());
        holder.txtdescripcion.setText(plato.getDescripcion());
        holder.txtprecio.setText(String.valueOf(plato.getPrecio()));

        Glide.with(activity).load(plato.getUrl()).into(holder.plato); //usamos el glide para cargar la imagen
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class platoHolder extends RecyclerView.ViewHolder {
        TextView txtid,txtnombre,txtprecio,txtdescripcion;
        CircleImageView plato;

        public platoHolder(@NonNull View itemView) {
            super(itemView);
            txtid = itemView.findViewById(R.id.item_id);
            txtnombre = itemView.findViewById(R.id.item_nombre);
            txtprecio = itemView.findViewById(R.id.item_precio);
            txtdescripcion = itemView.findViewById(R.id.item_descripcion);
            plato = itemView.findViewById(R.id.imagenplato);
        }
    }
}
