package com.example.rolity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyecto1.R;

import java.util.List;

public class ListAdaptador extends RecyclerView.Adapter<ListAdaptador.ViewHolder> {
    private Context contexto;
    private List<ListProducto> productos;
    private LayoutInflater inflater;

    public ListAdaptador(Context contexto, List<ListProducto> productos) {
        this.inflater = LayoutInflater.from(contexto);
        this.contexto = contexto;
        this.productos = productos;
    }

    @NonNull
    @Override
    public ListAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lista_productos, null);
        return new ListAdaptador.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdaptador.ViewHolder holder, int position) {
        holder.bindData(productos.get(position));
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void setItems(List<ListProducto> productos) {
        this.productos = productos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView nombrePatin, precioPatin;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imgPatin);
            nombrePatin = itemView.findViewById(R.id.nombrePatin);
            precioPatin = itemView.findViewById(R.id.precioPatin);
        }

        void bindData(final ListProducto producto) {
            imagen.setImageResource(R.drawable.patin);
            nombrePatin.setText(producto.getNombrePatin());
            precioPatin.setText(producto.getPrecioPatin());
        }
    }
}
