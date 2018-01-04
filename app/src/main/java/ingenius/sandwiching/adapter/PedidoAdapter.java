package ingenius.sandwiching.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import ingenius.sandwiching.MainActivity;
import ingenius.sandwiching.R;
import ingenius.sandwiching.menu.Pedidos;
import ingenius.sandwiching.model.Pedido;

/**
 * Created by CesarLiizcano on 9/12/2017.
 */

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.viewHolder>{

    List<Pedido> pedidoList;
    Context context;
    Pedidos fragmentPedido;
    DecimalFormat formatea = new DecimalFormat("###,###.##");

    int precio , cantidad, idProducto;

    public PedidoAdapter() {
    }

    public PedidoAdapter(List<Pedido> pedidoList, Context context) {
        this.pedidoList = pedidoList;
        this.context = context;
        this.fragmentPedido = fragmentPedido;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_pedido, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, final int position) {
        final Pedido pedido = pedidoList.get(position);

        holder.tvCantidadProducto.setText(""+pedido.getCantidad());
        holder.tvTitleProducto.setText(pedido.getNombrePro());
        holder.tvPrecioTotal.setText(""+pedido.getPrecioTotal());

        Glide.with(context).load(pedido.getImageURl()).into(holder.imgProducto);

        precio = pedido.getPrecioTotal();
        String formatPrecio = formatea.format(precio);
        formatPrecio = formatPrecio.replace(',', '.');
        holder.tvPrecioTotal.setText("$ " + formatPrecio);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idProducto = pedido.getIdProducto();
                pedidoList.remove(position);
                notifyDataSetChanged();

                List<Pedido> pedidoItem = Pedido.find(Pedido.class, "IdProducto= ?", ""+idProducto);
                Pedido delete = pedidoItem.get(0);
                delete.delete();

                MainActivity activity = (MainActivity) context;
                activity.TotalaPagarPedido();
                activity.emptyPedido();

            }
        });


        holder.btnSumar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cantidad = Integer.parseInt(holder.tvCantidadProducto.getText().toString());
                int canTotal = cantidad + 1;
                holder.tvCantidadProducto.setText(""+canTotal);

                precioSumaUnit(holder, position);

                MainActivity activity = (MainActivity) context;
                activity.TotalaPagarPedido();
                activity.emptyPedido();
            }
        });

        holder.btnRestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cantidad = Integer.parseInt(holder.tvCantidadProducto.getText().toString());

                if (cantidad > 1){
                    int canTotal = cantidad - 1;
                    holder.tvCantidadProducto.setText(""+canTotal);

                    precioRestaUnit(holder, position);

                    MainActivity activity = (MainActivity) context;
                    activity.TotalaPagarPedido();
                    activity.emptyPedido();

                }else {
                    idProducto = pedido.getIdProducto();
                    pedidoList.remove(position);
                    notifyDataSetChanged();

                    List<Pedido> pedidoItem = Pedido.find(Pedido.class, "IdProducto= ?", ""+idProducto);
                    Pedido delete = pedidoItem.get(0);
                    delete.delete();

                    MainActivity activity = (MainActivity) context;
                    activity.TotalaPagarPedido();
                    activity.emptyPedido();
                }
            }
        });

    }

    private void precioSumaUnit(viewHolder holder, int position) {
        Pedido pedido = pedidoList.get(position);

        int cantItem = Integer.parseInt(holder.tvCantidadProducto.getText().toString());
        int precioItem = pedido.getPrecio();
        int precioTotal = cantItem * precioItem ;

        String formatPrecio = formatea.format(precioTotal);
        formatPrecio = formatPrecio.replace(',', '.');
        holder.tvPrecioTotal.setText("$ " + formatPrecio);

        idProducto = pedido.getIdProducto();
        List<Pedido> pedidoItem = Pedido.find(Pedido.class, "IdProducto= ?", ""+idProducto);
        Pedido pedSuma = pedidoItem.get(0);

        pedSuma.setCantidad(pedSuma.getCantidad() + 1);
        pedSuma.setPrecioTotal(precioTotal);

        pedSuma.save();
    }

    private void precioRestaUnit(viewHolder holder, int position){
        Pedido pedido = pedidoList.get(position);

        int cantItem = Integer.parseInt(holder.tvCantidadProducto.getText().toString());
        int precioItem = pedido.getPrecio();
        int precioTotal = cantItem * precioItem ;

        String formatPrecio = formatea.format(precioTotal);
        formatPrecio = formatPrecio.replace(',', '.');
        holder.tvPrecioTotal.setText("$ " + formatPrecio);

        idProducto = pedido.getIdProducto();
        List<Pedido> pedidoItem = Pedido.find(Pedido.class, "IdProducto= ?", ""+idProducto);
        Pedido pedResta = pedidoItem.get(0);

        pedResta.setCantidad(pedResta.getCantidad() - 1);
        pedResta.setPrecioTotal(precioTotal);

        pedResta.save();
    }

    @Override
    public int getItemCount() {
        if (pedidoList == null){
            return 0;
        }
        return pedidoList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView imgProducto;
        Button btnDelete, btnSumar, btnRestar;
        TextView tvTitleProducto, tvCantidadProducto, tvPrecioTotal ;

        public viewHolder(View itemView) {
            super(itemView);

            imgProducto = itemView.findViewById(R.id.ivFotoProducto);
            tvTitleProducto = itemView.findViewById(R.id.tvTitleProducto);
            btnSumar = itemView.findViewById(R.id.btnSumar);
            btnRestar = itemView.findViewById(R.id.btnRestar);

            btnDelete = itemView.findViewById(R.id.btnEliminarProducto);
            tvCantidadProducto = itemView.findViewById(R.id.tvCantidadPedido);
            tvPrecioTotal = itemView.findViewById(R.id.tvPrecioPedido);
        }
    }
}
