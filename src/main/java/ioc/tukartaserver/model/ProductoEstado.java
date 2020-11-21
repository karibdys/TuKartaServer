/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.model;

/**
 *
 * @author Manu
 */
public class ProductoEstado {

private String idRegistro;
private String idProducto;
private String nombreProducto;
private String idPedido;
private Estado estado;

  public ProductoEstado() {
  }

  public ProductoEstado(String idRegistro, String idProducto, String nombreProducto, String idPedido, Estado estado) {
    this.idRegistro = idRegistro;
    this.idProducto = idProducto;
    this.nombreProducto = nombreProducto;
    this.idPedido = idPedido;
    this.estado = estado;
  }


  

  public String getIdRegistro() {
    return idRegistro;
  }

  public String getIdProducto() {
    return idProducto;
  }

  public String getIdPedido() {
    return idPedido;
  }

  public Estado getEstado() {
    return estado;
  }

  public void setNombreProducto(String nombreProducto) {
    this.nombreProducto = nombreProducto;
  }

  public String getNombreProducto() {
    return nombreProducto;
  }

  public void setIdRegistro(String idRegistro) {
    this.idRegistro = idRegistro;
  }

  public void setIdProducto(String idProducto) {
    this.idProducto = idProducto;
  }

  public void setIdPedido(String idPedido) {
    this.idPedido = idPedido;
  }

  public void setEstado(Estado estado) {
    this.estado = estado;
  }
  


}
