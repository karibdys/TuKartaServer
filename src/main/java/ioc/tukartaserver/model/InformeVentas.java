package ioc.tukartaserver.model;

import java.util.ArrayList;

/**
 * Clase que controla los datos de un Informe de Ventas estándar 
 * @author Manu Mora
 */
public class InformeVentas {

  private String nomRestaurante;
  private String nomGestor;
  
  ArrayList<Usuario> trabajadores;
  ArrayList<Integer> pedidos;
  ArrayList<Float> precios;
  
  private float precioTotal;
  private float precioMedio;
  
 /******************
   CONSTRUCTOR
 *******************/
  
  /**
   * Constructor básico que inicia los parámetros del InformeVentas
   */
  public InformeVentas(){
    trabajadores = new ArrayList();
    pedidos = new ArrayList();
    precios = new ArrayList();
  };
  
/******************
 GETTERS
******************/

public String getNomRestaurante() {
  return nomRestaurante;
}

public String getNomGestor() {
  return nomGestor;
}

public ArrayList<Usuario> getTrabajadores() {
  return trabajadores;
}

public ArrayList<Integer> getPedidos() {
  return pedidos;
}

public ArrayList<Float> getPrecios() {
  return precios;
}

public float getPrecioTotal() {
  return precioTotal;
}

public float getPrecioMedio() {
  return precioMedio;
}

/******************
SETTERS
******************/
public void setNomRestaurante(String nomRestaurante) {
  this.nomRestaurante = nomRestaurante;
}

public void setNomGestor(String nomGestor) {
  this.nomGestor = nomGestor;
}

public void setTrabajadores(ArrayList<Usuario> trabajadores) {
  this.trabajadores = trabajadores;
}

public void setPedidos(ArrayList<Integer> pedidos) {
  this.pedidos = pedidos;
}

public void setPrecios(ArrayList<Float> precios) {
  this.precios = precios;
}

public void setPrecioTotal(float precioTotal) {
  this.precioTotal = precioTotal;
}

public void setPrecioMedio(float precioMedio) {
  this.precioMedio = precioMedio;
}

/******************
AUXILIARES
*******************/

/**
 * Añade un empleado a la lista de empleados de este Informe
 * @param user Usuario a añadir
 */
public void addEmpleado(Usuario user){
  trabajadores.add(user);
}


/**
 * Añade un total de ventas al array Precios
 * @param precio float con el precio total a añadir 
 */
public void addPrecio(float precio){
  precios.add(precio);
}

/**
 * Añade un total de números de pedidos
 * @param numPedido int con el número total de pedidos
 */
public void addNumPedido(int numPedido){
  pedidos.add(numPedido);
}

/**
 * Calcula los datos restantes derivados de los informes insertados en el objeto. 
 */
public void cerrarPedido(){
  //cálculo del precio total de ventas:
  for (float euros : precios){
    precioTotal+= euros;
  }
  
  //cálculo del precio medio por pedido
  int totalPedidos =0;
  for (int numPed : pedidos){
    totalPedidos+= numPed;
  }
  precioMedio = precioTotal/totalPedidos;
  
}
}
