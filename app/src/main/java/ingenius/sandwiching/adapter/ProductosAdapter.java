package ingenius.sandwiching.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import ingenius.sandwiching.R;
import ingenius.sandwiching.model.Pedido;
import ingenius.sandwiching.model.Producto;

/**
 * Created by cesarlizcano on 17/11/17.
 */

public class ProductosAdapter extends RecyclerView.Adapter<ProductosAdapter.viewHolder> {

    List<Producto> productoList;
    Context context;

    DecimalFormat formatea = new DecimalFormat("###,###.##");
    int precio, idProducto, cantidad;
    String fomartPrecio;

    public ProductosAdapter(List<Producto> productoList, Context context) {
        this.productoList = productoList;
        this.context = context;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_producto, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, int position) {
        final Producto producto = productoList.get(position);

        precio = producto.getPrecioProducto();
        fomartPrecio = formatea.format(precio);
        fomartPrecio = fomartPrecio.replace(",", ".");

        holder.tvPrecioProducto.setText("$ "+fomartPrecio);
        holder.tvTitleProducto.setText(producto.getNombreProducto());
        holder.tvDescripcionProducto.setText(producto.getDescripcionProducto());

        Glide.with(context).load(producto.getImageUrl()).into(holder.imgProducto);

        List<Pedido> pedidoList = Pedido.listAll(Pedido.class);
        for (int i=0 ; i < pedidoList.size(); i++){
            if (pedidoList.get(i).getIdProducto() == producto.getIdProducto()){
                int cantPro = pedidoList.get(i).getCantidad();
                holder.btnCantidad.setVisibility(View.VISIBLE);
                holder.btnCantidad.setText(""+cantPro);
            }
        }

        //Click Listener Contenedor
        holder.contenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Pedido pedido = new Pedido();

                holder.btnCantidad.setVisibility(View.VISIBLE);

                cantidad = Integer.parseInt(holder.btnCantidad.getText().toString());
                idProducto = producto.getIdProducto();

                if (cantidad == 0){
                    cantidad++;

                    pedido.setIdProducto(idProducto);
                    pedido.setCantidad(cantidad);
                    pedido.setNombrePro(producto.getNombreProducto());
                    pedido.setDescripcion(producto.getDescripcionProducto());
                    pedido.setImageURl(producto.getImageUrl());
                    pedido.setPrecio(producto.getPrecioProducto());
                    pedido.setPrecioTotal(producto.getPrecioProducto());

                    pedido.save();

                }else if (cantidad > 0){
                    cantidad++;

                    List<Pedido> pedidoItem = Pedido.find(Pedido.class, "IdProducto= ?", ""+idProducto);
                    Pedido update = pedidoItem.get(0);

                    int precioUnit = update.getPrecio();
                    int precioTotal = cantidad * precioUnit;

                    update.setCantidad(cantidad);
                    update.setPrecioTotal(precioTotal);

                    update.save();

                }

                holder.btnCantidad.setText(""+cantidad);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        RelativeLayout contenedor ;
        ImageView imgProducto;
        TextView tvTitleProducto, tvDescripcionProducto, tvPrecioProducto;
        Button btnCantidad;

        public viewHolder(View itemView) {
            super(itemView);

            imgProducto = itemView.findViewById(R.id.ivFotoProducto);
            tvTitleProducto =  itemView.findViewById(R.id.tvTituloProducto);
            tvDescripcionProducto =  itemView.findViewById(R.id.tvDescripcionProducto);
            tvPrecioProducto =  itemView.findViewById(R.id.tvPrecioProducto);
            btnCantidad = itemView.findViewById(R.id.btnCantidad);
            contenedor = itemView.findViewById(R.id.contenedorProducto);
        }
    }
}
