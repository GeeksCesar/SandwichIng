package ingenius.sandwiching.conection;

/**
 * Created by cesarlizcano on 17/11/17.
 */

public class WebServices {

    public static final String TAG = "Test";

    private static final String HTTP = "http://geekdevelop.000webhostapp.com/sandwich/";
    private static final String PROJECT = "WebService/";

    public static final String REGISTRAR_USUARIO = HTTP + PROJECT + "setUsuario.php";
    public static final String CONSULTAR_PRODUCTOS = HTTP + PROJECT + "consultarProducto.php?idcategoria=";
    public static final String REGISTRAR_PEDIDO = HTTP + PROJECT + "setPedido.php";


    public static final String JSON_ID = "id";
    public static final String JSON_NOMBRE = "nombre";
    public static final String JSON_DESCRIPCION = "descripcion";
    public static final String JSON_PRECIO = "precio";
    public static final String JSON_IMAGEN = "img";

    public static final String RUTA_IMAGEN = HTTP + "public/sandwichLayout/files/";

}
