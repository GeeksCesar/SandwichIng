package ingenius.sandwiching;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ingenius.sandwiching.adapter.PedidoAdapter;
import ingenius.sandwiching.conection.WebServices;
import ingenius.sandwiching.conection.checkConection;
import ingenius.sandwiching.menu.Pedidos;
import ingenius.sandwiching.menu.Perfil;
import ingenius.sandwiching.model.Pedido;
import ingenius.sandwiching.utils.AlertMessage;

public class MainActivity extends AppCompatActivity {

    AlertMessage alertMessage = new AlertMessage();
    checkConection conection = new checkConection();

    DecimalFormat formatea = new DecimalFormat("###,###.##");

    public static final String NAME = "name";
    public static final String EMAIL = "email";

    //OPCIONES DEL MENU
    ListView navList;
    DrawerLayout drawer;
    Toolbar toolbar ;
    //_STRING OPCIONES DEL MENU
    final String[] MenuItems = {"Pedidos", "Mi Pedidos", "Vaciar Pedidos", "Tu perfil"};

    Typeface font;

    Bundle bundle ;
    String nameUser, emailUser ;

    Dialog dialogDelete;
    ImageView ivAlert;
    Button btnOkei, btnCancelar ;

    //Dialog Pedido
    Dialog dialogPedido ;
    TextView tvTitleDialog, tvTotalaPagar;
    Button btnContinuarPedido ;
    ImageView imgValidacion ;
    RecyclerView rvPedido;
    RecyclerView.LayoutManager layoutManager;
    PedidoAdapter pedidoAdapter ;
    ArrayList<Pedido> pedidoList = new ArrayList<>();
    List<Pedido> listCarrito = null;
    List<Pedido> listExternal ;

    //Dialog Datos
    Dialog dialogDatos ;
    EditText edNombres, edBarrio, edTelefono, edDireccion, edObservacion ;
    Button btnEnviarPedido;
    String  barrioUser, telefonoUser, direccionUser, observacionUser ;

    String listaPedidoFinal = "";
    int precioTotalCompra = 0;
    int lengthPhone ;

    int totalAPagar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        font = ResourcesCompat.getFont(this, R.font.font_poetsen);

        bundle = getIntent().getExtras();
        nameUser = bundle.getString(NAME);
        emailUser = bundle.getString(EMAIL);

        Log.d(WebServices.TAG, "nombre Usuario :" + nameUser);
        Log.d(WebServices.TAG, "email Usuario :" +emailUser);

        //_DRAWER - MENU DE LA APP
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_item, R.id.titleMenu, MenuItems );

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navList = (ListView) findViewById(R.id.drawer);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setFragment(0);

        navList.setAdapter(adapter);

        navList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
                switch (pos) {
                    case 0:
                        drawer.closeDrawer(navList);
                        toolbar.setTitle(getString(R.string.title_pedido));
                        setFragment(0);
                        break;
                    case 1:
                        drawer.closeDrawer(navList);
                        showDialogTuPedido();
                        break;
                    case 2:
                        drawer.closeDrawer(navList);
                        showDeletePedido();
                        break;

                    case 3:
                        drawer.closeDrawer(navList);
                        toolbar.setTitle(getString(R.string.title_mi_perfil));
                        setFragment(1);
                        break;

                }
            }
        });
    }

    public void showDeletePedido(){
        dialogDelete = new Dialog(this);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setContentView(R.layout.dialog_vaciar_pedido);
        dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ivAlert = dialogDelete.findViewById(R.id.imageViewVaciarPedido);
        btnOkei = dialogDelete.findViewById(R.id.buttonAceptar);
        btnCancelar = dialogDelete.findViewById(R.id.buttonCancelar);

        ivAlert.setImageResource(R.mipmap.alerta_vaciar_pedido);

        listCarrito = Pedido.listAll(Pedido.class);
        if (listCarrito.size() ==  0){
            btnOkei.setVisibility(View.GONE);
            btnCancelar.setGravity(Gravity.CENTER);
        }else {
            btnOkei.setVisibility(View.VISIBLE);
        }

        btnOkei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePedido();
                dialogDelete.dismiss();
            }
        });


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDelete.cancel();
            }
        });

        dialogDelete.show();
    }

    /**
     * Dialog TU PEDIDO
     */
    public void showDialogTuPedido(){
        dialogPedido = new Dialog(this, R.style.FullScreenDialogStyle);
        dialogPedido.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogPedido.setContentView(R.layout.dialog_tu_pedido);
        dialogPedido.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvTotalaPagar = dialogPedido.findViewById(R.id.tvTotalPrecio);
        btnContinuarPedido = dialogPedido.findViewById(R.id.btnContinuarPedido);
        imgValidacion = dialogPedido.findViewById(R.id.ivValidacion);
        rvPedido = dialogPedido.findViewById(R.id.rvPedido);

        rvPedido.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvPedido.setLayoutManager(layoutManager);

        dataCarrito();
        emptyPedido();

        btnContinuarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalAPagar == 0){
                    alertMessage.showSnackAlert(view, getString(R.string.alert_pedido_vacio));
                }else {
                    showDialogDatosEnvio();
                }
            }
        });

        dialogPedido.show();
    }

    public void dataCarrito(){
        listCarrito = Pedido.listAll(Pedido.class);
        TotalaPagarPedido();
        listExternal = listCarrito;
        //Adapter
        pedidoAdapter = new PedidoAdapter(listCarrito, this);
        rvPedido.setAdapter(pedidoAdapter);
    }

    public void TotalaPagarPedido(){
        totalAPagar = 0;

        List<Pedido> productListPedido = Pedido.listAll(Pedido.class);

        for (int i=0; i<productListPedido.size(); i++){
            int precio = productListPedido.get(i).getPrecio();
            int cant = productListPedido.get(i).getCantidad();
            int valorTotal = precio * cant;

            totalAPagar = totalAPagar + valorTotal ;
        }
        String formatPrecio = formatea.format(totalAPagar);
        formatPrecio = formatPrecio.replace(',', '.');

        tvTotalaPagar.setText(""+formatPrecio);
    }

    public void emptyPedido(){
        long count = Pedido.count(Pedido.class);
        if (count == 0){
            imgValidacion.setVisibility(View.VISIBLE);
            Log.d(WebServices.TAG, " pedido vacio: "+count);
        }else {
            Log.d(WebServices.TAG, " count item: "+count);
            imgValidacion.setVisibility(View.INVISIBLE);
        }
    }
    private void deletePedido(){
        Pedido.deleteAll(Pedido.class);
    }
    private void showDialogDatosEnvio() {
        dialogPedido.cancel();

        dialogDatos = new Dialog(this, R.style.FullScreenDialogStyle);
        dialogDatos.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDatos.setContentView(R.layout.dialog_datos);
        dialogDatos.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        edBarrio = dialogDatos.findViewById(R.id.edBarrioUser);
        edDireccion = dialogDatos.findViewById(R.id.edDireccionUser);
        edTelefono = dialogDatos.findViewById(R.id.edTelefonoUser);
        edObservacion = dialogDatos.findViewById(R.id.edObservacionUser);
        btnEnviarPedido = dialogDatos.findViewById(R.id.btnEnviarPedido);


        btnEnviarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                barrioUser = edBarrio.getText().toString().trim();
                direccionUser = edDireccion.getText().toString().trim();
                telefonoUser = edTelefono.getText().toString().trim();
                observacionUser = edObservacion.getText().toString().trim();

                lengthPhone = edTelefono.length();

                listaPedidoFinal = "";
                precioTotalCompra = 0;

                List<Pedido> pedidoFinal = Pedido.listAll(Pedido.class);
                for (int i = 0; i < pedidoFinal.size(); i++) {
                    String producto = pedidoFinal.get(i).getCantidad() + " " + pedidoFinal.get(i).getNombrePro();
                    listaPedidoFinal = listaPedidoFinal + producto + ",";
                    int precio = pedidoFinal.get(i).getPrecio() * pedidoFinal.get(i).getCantidad();
                    precioTotalCompra = precioTotalCompra + precio;
                }

                listaPedidoFinal = listaPedidoFinal.substring(0, listaPedidoFinal.length() - 1);
                Log.d(WebServices.TAG, "lista Pedido: "+listaPedidoFinal);
                Log.d(WebServices.TAG, "precioTotal Pedido: "+precioTotalCompra);

                if (telefonoUser.equals("") || barrioUser.equals("") || direccionUser.equals("")){
                    alertMessage.showSnackAlert(view, getString(R.string.alert_input_vacio));
                }else if (lengthPhone != 7 && lengthPhone != 10){
                    alertMessage.showSnackAlert(view, getString(R.string.alert_telefono_error));
                }else {
                    if (conection.verificaConexion(MainActivity.this)){

                    }else {
                        Log.d(WebServices.TAG, "No hay Conexion");
                    }
                }

            }
        });

        dialogDatos.show();
    }


    private void setFragment(int position){

        Fragment fragment = null;

        switch (position){

            case 0:
                fragment = new Pedidos();
                getSupportFragmentManager().beginTransaction().replace(R.id.main, fragment, "PEDIDO").commit();
                break;

            case 1:
                fragment = new Perfil();
                getSupportFragmentManager().beginTransaction().replace(R.id.main, fragment,"PEDIDO").commit();
                break;
        }
    }

}
