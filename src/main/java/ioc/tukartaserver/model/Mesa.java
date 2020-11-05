/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.model;

import java.util.ArrayList;

/**
 * Clase que representa una Mesa del Restaurante
 * @author Manu Mora
 */
public class Mesa {

private String id;
private int num_comensales;
private ArrayList<Pedido> listaPedidos;


/******************
 * CONSTRUCTOR
 ******************
 */

/**
 * Crea un objeto de tipo mesa. Requiera su ID y el número de comensales
 * @param id String id de la mesa
 * @param numComensajes int número de comensales que tiene la mesa
 */
public Mesa(String id, int numComensales) {
  this.id = id;
  this.num_comensales = numComensales;
}


/******************
 * GETTERS
 ******************
 */

/**
 * Devuelve el ID de la mesa 
 * @return String con el ID de la mesa
 */
public String getId() {
  return id;
}

/**
 * Devuelve el número de comensales de la mesa
 * @return int con el número de comensales
 */
public int getNum_comensales() {
  return num_comensales;
}

/**
 * Devuelve un listado de los pedidos que tiene la mesa, tanto abiertos como cerrados.
 * @return 
 */
public ArrayList<Pedido> getListaPedidos() {
  return listaPedidos;
}

/**
 * Devuelve todos los pedidos cerrados que tiene la mesa.
 * @return ArrayList<Pedido> con los pedidos que se han cerrado en esta mesa
 */
public ArrayList<Pedido> getPedidosCerrados(){
  ArrayList<Pedido> ret = new ArrayList<>();
  for (Pedido ped : listaPedidos){
    if (!ped.isActivo()){
      ret.add(ped);
    }
  }
  return ret;
}

/**
 * Devuelve el pedido Activo de la Mesa. Solo puede haber un pedido activo.
 * @return Pedido el último pedido activo, si lo hay. Devuelve null si todos están cerrados. 
 */
public Pedido getPedidoActivo(){
  Pedido ret = null;
  for (Pedido ped : listaPedidos){
    if (ped.isActivo()){
      ret = ped;
      break;
    }
  }
  return ret;
}

/******************
 * SETTERS
 ******************
 */

/**
 * Establece el ID de una Mesa
 * @param id String con el ID de una mesa
 */
public void setId(String id) {
  this.id = id;
}

/**
 * Establece el número de comensales de una mesa
 * @param num_comensales int número de comensales
 */
public void setNum_comensales(int num_comensales) {
  this.num_comensales = num_comensales;
}

/**
 * Establece un listado de pedidos prehecho en la mesa
 * @param listaPedidos ArrayLis<Pedido> con los pedidos que le corresponden a la mesa
 */
public void setListaPedidos(ArrayList<Pedido> listaPedidos) {
  this.listaPedidos = listaPedidos;
}

/******************
 * MÉTODOS AUXILIARES
 ******************
 */

/**
 * Confirma si la Mesa actual tiene un pedido activo o no. 
 * @return true si hay pedidos activos, false si no los hay
 */
public boolean tienePedidoActivo(){
  if (this.getPedidoActivo()==null){
    return false;    
  }else{
    return true;
  }
}

/**
 * Confirma si hay un Pedido concreto en el listado
 * @param ped Pedido a comprobar
 * @return true si el pedido está en el listado, false si no
 */
public boolean hasPedido (Pedido ped){
  return listaPedidos.contains(ped);
}

/**
 * Confirma si hay un Pedido concreto en el listando pasándo como parámetro su ID
 * @param idPedido String con el ID del Pedido
 * @return true si el ID del Pedido está en el listado, false si no- 
 */
public boolean hasPedido(String idPedido){
  boolean ret = false;
  for (Pedido ped: listaPedidos){
    if (ped.getId().equals(idPedido)){
      ret=true;
      break;
    }
  }
  return ret;
}

/**
 * Añade un nuevo Pedido a la mesa siempre y cuando no haya un pedido activo en ella. 
 * @param ped Pedido a añadir a la mesa
 * @return true si se añade el Pedido y false si hay un Pedido activo y no se añade.
 */
public boolean addPedido(Pedido ped){
  boolean ret = false;
  if (!this.tienePedidoActivo()){
    listaPedidos.add(ped);
    ret = true;   
  }
  return ret;
}

/**
 * Elimina un Pedido del listado de pedidos si existe. 
 * @param ped Pedido a eliminar del listado
 * @return true si el Pedido se elimina correctamente, false si el Pedido no está en el listado
 */
public boolean removePedido(Pedido ped){
  boolean ret = false;
  if (listaPedidos.contains(ped)){
    listaPedidos.remove(ped);
    ret = true;
  }
  return ret;
}

/**
 * Elimina un pedido que se corresponde con una ID que se pasa como parámetro. 
 * @param idPedido String con el ID del pedido
 * @return true si el Pedido se elimina del listado, false si el ID no se corresponde con ningún Pedido del listado.
 */
public boolean removePedido (String idPedido){
  boolean ret = false;
  for (Pedido ped : listaPedidos){
    if (ped.getId().equals(idPedido)){
      listaPedidos.remove(ped);
      ret = true;      
      break;
    }    
  }
  return ret;
}
  
}
