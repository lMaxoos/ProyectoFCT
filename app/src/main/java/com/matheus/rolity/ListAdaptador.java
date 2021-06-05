package com.matheus.rolity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ListAdaptador extends RecyclerView.Adapter<ListAdaptador.ViewHolder> {
    private Context contexto;
    private List<Producto> productos;
    private LayoutInflater inflater;
    private RecyclerView rv;
    private boolean carrito;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ListAdaptador(Context contexto, List<Producto> productos, RecyclerView rv, boolean carrito) {
        this.inflater = LayoutInflater.from(contexto);
        this.contexto = contexto;
        this.productos = productos;
        this.rv = rv;
        this.carrito = carrito;
    }

    @NonNull
    @Override
    public ListAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lista_productos, null);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = rv.getChildLayoutPosition(v);
                Intent abrirProducto = new Intent(contexto, ActivityProducto.class);
                abrirProducto.putExtra("producto", productos.get(pos));
                contexto.startActivity(abrirProducto);
            }
        });

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

    public void setItems(List<Producto> productos) {
        this.productos = productos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        ImageView imagen;
        TextView nombreProducto, precioProducto;
        LinearLayout numProductos;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imgProducto);
            nombreProducto = itemView.findViewById(R.id.nombreProducto);
            precioProducto = itemView.findViewById(R.id.precioProducto);
            numProductos = itemView.findViewById(R.id.numeroProductosCarrito);
            if (carrito)
                numProductos.setVisibility(View.VISIBLE);
            else
                numProductos.setVisibility(View.GONE);
        }

        void bindData(final Producto producto) {
            Glide.with(contexto)
                    .load(producto.getImagen())
                    .into(imagen);
            nombreProducto.setText(producto.getNombreProducto());
            precioProducto.setText(producto.getPrecioProducto());
        }

    }
}
