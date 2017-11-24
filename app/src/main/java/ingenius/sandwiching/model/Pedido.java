package ingenius.sandwiching.model;

import com.orm.SugarApp;
import com.orm.dsl.Column;
import com.orm.dsl.Table;

/**
 * Created by cesarlizcano on 17/11/17.
 */

@Table(name = "Pedido")
public class Pedido extends SugarApp{

    @Column(name = "IdProducto")
    private int idProducto ;

    @Column(name = "nombre")
    private String nombrePro ;

    @Column(name = "precio")
    private int precio ;

    @Column(name = "image")
    private String imageURl ;

    @Column(name = "cantidad")
    private int cantidad ;

    @Column(name = "descripcion")
    private int descripcion ;

    @Column(name = "precioTotal")
    private int precioTotal ;

    public Pedido() {
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombrePro() {
        return nombrePro;
    }

    public void setNombrePro(String nombrePro) {
        this.nombrePro = nombrePro;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(int descripcion) {
        this.descripcion = descripcion;
    }

    public int getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(int precioTotal) {
        this.precioTotal = precioTotal;
    }
}
