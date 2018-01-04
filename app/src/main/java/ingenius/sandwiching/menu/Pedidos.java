package ingenius.sandwiching.menu;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ingenius.sandwiching.MainActivity;
import ingenius.sandwiching.R;
import ingenius.sandwiching.adapter.ProductosAdapter;
import ingenius.sandwiching.conection.WebServices;
import ingenius.sandwiching.conection.checkConection;
import ingenius.sandwiching.model.Producto;
import ingenius.sandwiching.utils.AlertMessage;


public class Pedidos extends Fragment {

    View view;
    Context context;
    AlertMessage alertMessage = new AlertMessage();
    checkConection conection = new checkConection();

    //Configuracion Recyclerview
    CoordinatorLayout contenedorPrincipal ;
    Button btnBebidas, btnSandwich ;
    RecyclerView recyclerView;
    ProductosAdapter productoAdapter;
    ArrayList<Producto> productosList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton fabTuPedido;

    //Volley
    String URlProductos, UrlPedido;
    RequestQueue requestQueue;
    StringRequest stringRequest;
    JsonArrayRequest jsonArrayRequest;

    int idProducto, precioProducto;
    String imgUrlProducto, nombreProducto, descripcionProducto;


    public Pedidos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.menu_pedido, container, false);

        context = getActivity();

        contenedorPrincipal= view.findViewById(R.id.contenedorPrincipal);
        fabTuPedido = view.findViewById(R.id.fab);
        btnBebidas = view.findViewById(R.id.btnBebidas);
        btnSandwich =  view.findViewById(R.id.btnSandwich);
        recyclerView =  view.findViewById(R.id.recyclerview);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
      //  recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        btnSandwich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSandwich.setPressed(false);
                btnBebidas.setPressed(true);

                DatalistRecyclerView(1);
            }
        });

        btnBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSandwich.setPressed(true);
                btnBebidas.setPressed(false);

                DatalistRecyclerView(2);
            }
        });

        DatalistRecyclerView(1);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    if (fabTuPedido.isShown()) {
                        fabTuPedido.hide();
                    }
                } else if (dy < 0) {
                    if (!fabTuPedido.isShown()) {
                        fabTuPedido.show();
                    }
                }
            }

        });

        //SetClistener FAB
        fabTuPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) getActivity();
                activity.showDialogTuPedido();
            }
        });

        return view;
    }

    private void DatalistRecyclerView(int idCategoria) {
        productosList.clear();

        requestQueue = Volley.newRequestQueue(getActivity());

        URlProductos = WebServices.CONSULTAR_PRODUCTOS + idCategoria;
        Log.d(WebServices.TAG, "UrlProducto: " + URlProductos);

        jsonArrayRequest = new JsonArrayRequest(URlProductos, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                getDataProductos(jsonArray);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof TimeoutError){

                }else if (volleyError instanceof ServerError){

                }else if (volleyError instanceof NoConnectionError){

                }else if (volleyError instanceof NetworkError){

                }
            }
        });

        requestQueue.add(jsonArrayRequest);

    }

    private void getDataProductos(JSONArray jsonArray) {
        if (jsonArray.length() > 0) {
            Log.d(WebServices.TAG, "array: " + jsonArray);

            for (int i = 0; i < jsonArray.length(); i++) {
                Producto producto = new Producto();

                JSONObject object = null;

                try {
                    object = jsonArray.getJSONObject(i);

                    producto.setIdProducto(object.getInt(WebServices.JSON_ID));
                    producto.setNombreProducto(object.getString(WebServices.JSON_NOMBRE));
                    producto.setDescripcionProducto(object.getString(WebServices.JSON_DESCRIPCION));
                    producto.setPrecioProducto(object.getInt(WebServices.JSON_PRECIO));
                    producto.setImageUrl(WebServices.RUTA_IMAGEN + object.getString(WebServices.JSON_IMAGEN));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                productosList.add(producto);
            }

            //Adapter Recyclerview
            productoAdapter = new ProductosAdapter(productosList, getActivity());
            recyclerView.setAdapter(productoAdapter);

        } else {
            Log.d(WebServices.TAG, "no hay datos");
        }
    }


}
