package com.matheus.rolity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ListAdaptador extends RecyclerView.Adapter<ListAdaptador.ViewHolder> {
    private Context contexto;
    private List<Producto> productos;
    private LayoutInflater inflater;
    private RecyclerView rv;
    private boolean carrito;
    private ArrayList<String> carritoNum;
    private FirebaseFirestore db;
    private FirebaseUser usr;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ListAdaptador(Context contexto, List<Producto> productos, ArrayList<String> carritoNum, RecyclerView rv, boolean carrito) {
        this.inflater = LayoutInflater.from(contexto);
        this.contexto = contexto;
        this.productos = productos;
        this.rv = rv;
        this.carrito = carrito;
        this.carritoNum = carritoNum;
        db = FirebaseFirestore.getInstance();
        usr = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ListAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_productos, null);

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
        if (carritoNum != null) {
            holder.bindData(productos.get(position), carritoNum);
        } else
            holder.bindData(productos.get(position), null);

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    public void setItems(List<Producto> productos) {
        this.productos = productos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        String path;
        ImageView imagen;
        TextView nombreProducto, precioProducto;
        LinearLayout layoutNumProductos;
        EditText numProductos;
        String numProductoCortado = "";
        double precioOriginal;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imgProducto);
            nombreProducto = itemView.findViewById(R.id.nombreProducto);
            precioProducto = itemView.findViewById(R.id.precioProducto);
            layoutNumProductos = itemView.findViewById(R.id.layoutNumeroProductos);
            numProductos = itemView.findViewById(R.id.numeroProductosProducto);
            if (carrito)
                layoutNumProductos.setVisibility(View.VISIBLE);
            else
                layoutNumProductos.setVisibility(View.GONE);
        }

        void bindData(final Producto producto, ArrayList<String> lista) {
            path = producto.getCategoria() + "/" + producto.getNombreProducto();

            Glide.with(contexto)
                    .load(producto.getImagen())
                    .into(imagen);
            nombreProducto.setText(producto.getNombreProducto());
            if (carrito) {
                for (int i = 0; i < lista.size(); i++) {
                    if (lista.get(i).contains(path)) {
                        numProductoCortado = "" + lista.get(i).charAt(lista.get(i).length()-1);
                    }
                }

                double dPrecio = Double.parseDouble(producto.getPrecioProducto());
                precioOriginal = dPrecio;
                dPrecio = dPrecio * Double.parseDouble(numProductoCortado);
                dPrecio = Math.round(dPrecio*100.0)/100.0;
                String precio = dPrecio + " €";
                precioProducto.setText(precio);
                numProductos.setText(numProductoCortado);
            } else {
                String precio = producto.getPrecioProducto() + " €";
                precioProducto.setText(precio);
            }

            Button botonMas = itemView.findViewById(R.id.botonMasProducto);
            Button botonMenos = itemView.findViewById(R.id.botonMenosProducto);

            botonMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double auxNumProductos = Double.parseDouble(numProductoCortado);
                    if (auxNumProductos != 9) {
                        double dPrecio = Double.parseDouble(precioProducto.getText().toString().substring(0, producto.getPrecioProducto().length()-2));
                        dPrecio = Math.round(dPrecio*100.0)/100.0;
                        dPrecio +=  precioOriginal;
                        String precio = dPrecio + " €";
                        precioProducto.setText(precio);
                        int aux = (int)auxNumProductos;
                        numProductos.setText(String.valueOf((int) ++auxNumProductos));

                        DocumentReference array = db.collection("usuarios").document(usr.getEmail());
                        array.update("carrito", FieldValue.arrayUnion(path));
                        array.update("carrito_num", FieldValue.arrayRemove(path + "/" + aux));
                        array.update("carrito_num", FieldValue.arrayUnion(path + "/" + (int)auxNumProductos));

                        numProductoCortado = "" + auxNumProductos;
                    }
                }
            });

            botonMenos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double auxNumProductos = Double.parseDouble(numProductoCortado);
                    if (auxNumProductos != 1) {
                        double dPrecio = Double.parseDouble(precioProducto.getText().toString().substring(0, producto.getPrecioProducto().length()-2));
                        dPrecio = Math.round(dPrecio*100.0)/100.0;
                        dPrecio -= precioOriginal;
                        String precio = dPrecio + " €";
                        precioProducto.setText(String.valueOf(dPrecio));
                        int aux = (int)auxNumProductos;
                        numProductos.setText(String.valueOf((int) --auxNumProductos));

                        DocumentReference array = db.collection("usuarios").document(usr.getEmail());
                        array.update("carrito", FieldValue.arrayUnion(path));
                        array.update("carrito_num", FieldValue.arrayRemove(path + "/" + aux));
                        array.update("carrito_num", FieldValue.arrayUnion(path + "/" + (int)auxNumProductos));

                        numProductoCortado = "" + auxNumProductos;
                    } else {
                        mostrarDialog();
                    }
                }
            });

        }

        private void mostrarDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Eliminar producto");
            builder.setMessage("¿Desea eliminar este producto de la lista?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DocumentReference array = db.collection("usuarios").document(usr.getEmail());
                            array.update("carrito", FieldValue.arrayRemove(path));
                            array.update("carrito_num", FieldValue.arrayRemove(path + "/" + numProductos.getText().toString()));
                            Toast.makeText(itemView.getContext(), "Producto eliminado!", Toast.LENGTH_SHORT).show();
                            productos.remove(getAdapterPosition());
                            notifyItemRemoved(getAdapterPosition());
                            notifyItemRangeChanged(getAdapterPosition(), productos.size());

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(true)
                    .show();
        }

    }
}
