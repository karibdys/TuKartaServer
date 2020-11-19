package ioc.tukartaserver.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Clase que representa a un pedido que genera un empleado en una mesa determinada. 
 * @author Manu Mora
 */
public class Pedido {

private String id;
//private ArrayList<String> lista_id;
private ArrayList<Producto> lista_productos;
private ArrayList<Estado> estado_productos;
private Empleado empleado;
private Date fecha;
private Mesa mesa;  
private float precio_final;
private boolean activo;


/******************
 * CONSTRUCTOR
 ******************
 */

/**
 * Constructor básico de la clase. Necesita ser asignado a un empleado y a una mesa 
 * @param empleado Empleado que genera el Pedido
 * @param mesa Mesa en le que se sirve el Pedido
 */
public Pedido(Empleado empleado, Mesa mesa) {    
  this.empleado = empleado;
  this.mesa=mesa;    
  //automáticamente crea un Pedido activo, con fecha actual y con un ID generado automáticamente
  this.activo=true;
  this.fecha=new Date();  
  this.id = generarId(mesa);
}

/**
 * Constructor completo de la clase. 
 * @param id String con el id del pedido
 * @param empleado Empleado que genera el Pedido
 * @param mesa Mesa en le que se sirve el Pedido
 * @param fecha Date con la fecha del pedido
 */
  public Pedido(String id, Empleado empleado, Date fecha, Mesa mesa) {
    this.id = id;
    this.empleado = empleado;
    this.fecha = fecha;
    this.mesa = mesa;
  }

  public Pedido() {    
  }

/******************
   * GETTERS
   ******************
   */

/**
 * Devuelve el listado de productos que hay en el Pedido
 * @return ArrayList<Producto> listado con todos los productos que están en el Pedido
 */
public ArrayList<Producto> getLista_productos(){
  return lista_productos;
}

/**
 * Devuelve un listado con el estado de los objetos Pedido
 * @return ArrayList<Estado> los estados de los pedidos en el listado
 */
public ArrayList<Estado> getEstado_productos() {
  return estado_productos;
}

/**
 * Devuelve el empleado asignado a este Pedido
 * @return Empleado asignado al pedido
 */
public Empleado getEmpleado() {
  return empleado;
}

/**
 * Devuelve la fecha de creación del Pedido
 * @return Date con la fecha de creación del pedido
 */
public Date getFecha() {
  return fecha;
}

/**
 * Devuelve la Mesa en la que se está sirviendo el Pedido
 * @return Mesa   
 */
public Mesa getMesa() {
  return mesa;
}

/**
 * Devuelve el precio final del Pedido
 * @return float con el precio final del pedido. Es la suma de todos sus productos.
 */
public float getPrecio_final() {
  return precio_final;
}

/**
 * Devuelve si el pedido está Activo o se ha cerrado
 * @return true si está Activo, false si está cerrado
 */
public boolean isActivo() {
    return activo;
  }

/**
 * Devuelve el id del Pedido
 * @return String con el Id del Pedido
 */
  public String getId() {
    return this.id;
  }


/******************
 * SETTERS
 ******************
 */

/**
 * Establece un ID para este Pedido
 * @param id 
 */
public void setId(String id){
  this.id=id;
}
  
/**
 * Establece una lista con de objetos Producto generada externamente al Pedido
 * @param Lista_productos ArrayList<Producto> con los objetos Producto del Pedido
 */  
public void setLista_productos(ArrayList<Producto> Lista_productos) {
  this.lista_productos = Lista_productos;
}

/**
 * Establece una lista de objetos Estado generado externamente al pedido. Se relacionan con la lista de Producto
 * @param estado_productos  ArrayList<Estado> con los Estado de los Producto del Pedido
 */
public void setEstado_productos(ArrayList<Estado> estado_productos) {
  this.estado_productos = estado_productos;
}

/**
 * Establece el Empleado asignado al Pedido
 * @param empleado Empleado asignado al Pedido
 */
public void setEmpleado(Empleado empleado) {
  this.empleado = empleado;
}

/**
 * Establece la Mesa a la que se sirve el Pedido
 * @param mesa Mesa a la que se sirve el Pedido
 */
public void setMesa(Mesa mesa) {
  this.mesa = mesa;
}

/**
 * Establece un precio final que nos e genera automáticamente al sumar los productos
 * @param precio_final float precio final del Pedido
 */
public void setPrecio_final(float precio_final) {
  this.precio_final = precio_final;
}

/**
 * Establece una fecha de creación del pedido
 * @param fecha Date con la fecha a establecer
 */
public void setFecha(Date fecha){
  this.fecha=fecha;
}

/**
 * Establece si el pedido está activo o no
 * @param activo boolean true si está activo, false si está cerrado
 */
public void setActivo(boolean activo) {
  this.activo = activo;
}

/******************
   * MÉTODOS AUXILIARES
   ******************
   */

/**
 * Genera un ID basándose en la mesa y la fecha en la que se ha generado el pedido
 * @param mesa Mesa en la que se asigna el Pedido
 * @return String con un ID único basado en la fecha y la Mesa del Pedido
 */
public static String generarId(Mesa mesa) {
  String ret = "";
  Date fecha = new Date();
  SimpleDateFormat formato = new SimpleDateFormat("YYMMdd/HHmm");
  ret = formato.format(fecha)+"-"+mesa.getId();
  return ret;
}
  
/**
 * Añade un Producto al listado de Productos, un Estado al listado de estados y actualiza el precio final del Pedido
 * @param prod 
 */
public void addProducto(Producto prod){
  //añade el producto y el estado
  lista_productos.add(prod);
  estado_productos.add(Estado.NO_INICIADO);
  //calcula el precio
  actualizarPrecio();
}

/**
 * Elimina un Producto del listado (y su Estado equivalente) según la posición que ocupa
 * @param posicionProd int con la posición del Producto en la lista de productos
 */
public void removeProducto(int posicionProd){
  lista_productos.remove(posicionProd);
  estado_productos.remove(posicionProd);    
  actualizarPrecio();
}

/**
 * Cambia el estado de un Producto determinado según la posición que ocupa
 * @param posicionProd int con la posición del Producto en la lista de productos
 * @param estado Estado al que cambiamos el Producto
 */
public void cambiarEstadoProducto(int  posicionProd, Estado estado){
  estado_productos.set(posicionProd, estado);
}

/**
 * Actualiza el precio final del Pedido sumando los precios de todos los objeto Producto del listado
 */
public void actualizarPrecio(){
  precio_final = 0;
  for (Producto prod : lista_productos){
    precio_final += prod.getPrecio();
  }
}

}


