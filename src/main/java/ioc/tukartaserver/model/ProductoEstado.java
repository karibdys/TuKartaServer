package ioc.tukartaserver.model;

/**
 * Clase que representa el estado de un producto dentro de un pedido
 * @author Manu Manu
 */
public class ProductoEstado {

private String idRegistro;
private String idProducto;
private String nombreProducto;
private String idPedido;
private Estado estado;

/******************
 * CONSTRUCTOR
 *******************/

/**
 * Constructor básico de la clase sin parámetros
 */
public ProductoEstado() {
}

/**
 * Constructor de un objeto de la clase con todos los parámetros necesarios para ello
 * @param idRegistro String con el id del registro del propio producto en el pedido
 * @param idProducto String con el id del producto
 * @param nombreProducto String con el nombre del producto
 * @param idPedido String con el id del pedido al que se asocia el producto
 * @param estado  Estado con el estado actual del producto
 */
public ProductoEstado(String idRegistro, String idProducto, String nombreProducto, String idPedido, Estado estado) {
  this.idRegistro = idRegistro;
  this.idProducto = idProducto;
  this.nombreProducto = nombreProducto;
  this.idPedido = idPedido;
  this.estado = estado;
}


/******************
 * GETTERS
 *******************/

/**
 * Devuelve el id de registro
 * @return String
 */
public String getIdRegistro() {
  return idRegistro;
}

/**
 * Devuelve el id del producto
 * @return String
 */
public String getIdProducto() {
  return idProducto;
}

/**
 * Devuelve el id del pedido al que se asocia el producto
 * @return String
 */

public String getIdPedido() {
  return idPedido;
}

/**
 * Devuelve el Estado del producto
 * @return Estado
 */
public Estado getEstado() {
  return estado;
}

/**
 * Devuelve el nombre del producto
 * @return String
 */
public String getNombreProducto() {
  return nombreProducto;
}

/******************
 * SETTERS
 *******************/

/**
 * Establece un id de registro del producto
 * @param idRegistro String con el id del registro del producto en este pedido
 */
public void setIdRegistro(String idRegistro) {
  this.idRegistro = idRegistro;
}

/**
 * Establece un id de producto
 * @param idProducto String con el id del producto 
 */
public void setIdProducto(String idProducto) {
  this.idProducto = idProducto;
}

/**
 * Establece un id de pedido al que se le asocia el producto y su estado
 * @param idPedido String con el id del pedido
 */
public void setIdPedido(String idPedido) {
  this.idPedido = idPedido;
}

/**
 * Establece el estado del producto en este pedido
 * @param estado Estado
 */
public void setEstado(Estado estado) {
  this.estado = estado;
}
  
/**
 * Establece el nombre del Producto. No usar a menos que no se pueda recuperar de la base de datos ya que no guardará los cambios en ella. 
 * @return String
 */
public void setNombreProducto(String nombreProducto) {
  this.nombreProducto = nombreProducto;
}

}
