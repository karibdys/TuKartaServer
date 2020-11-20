/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.model;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Clase que representa un Producto en la base de datos
 * @author Manu Mora
 */
public class Producto {

protected String id;
protected String nombre;
protected HashSet<Alergeno> alergenos = new HashSet<>();
protected float precio;
protected int disponibles;
protected int tiempo_elaboracion;
protected float precio_real;
protected ArrayList<Producto> contenido = new ArrayList<>();
private byte[] imagen;

/******************
 * CONSTRUCTOR
 ******************
 */

/**
 * Constructor básico sin parámetros. Crea un producto vacío.
 */
public Producto(){};

/**
 * Constructor que requiere todos los parámetros del objeto. Crea un producto completo.
 * @param id String id del producto en la base de datos
 * @param nombre String nombre del producto
 * @param alergeno Alergeno tipo de alérgeno que contiene
 * @param precio float precio del producto
 * @param disponibles int cantidad de producto disponible
 * @param tiempoElaboracion int tiempo (en minutos) de elaboración
 */
public Producto(String id, String nombre, HashSet<Alergeno> alergeno, float precio, int disponibles, int tiempoElaboracion) {
  this.id = id;
  this.nombre = nombre;
  this.alergenos = alergeno;
  this.precio = precio;
  this.disponibles = disponibles;
  this.tiempo_elaboracion = tiempoElaboracion;
}

/******************
 * GETTERS
 ******************
 */
/**
 * Devuelve el ID del producto
 * @return String con el ID del producto
 */
public String getId() {
  return id;
}

/**
 * Devuelve el nombre del producto
 * @return String con el nombre del Producto
 */
public String getNombre() {
  return nombre;
}

/**
 * Devuelve un listado con los alérgnos que tiene este Producto
 * @return ArrayList<Alergeno> un listado con los alérgnos del producto
 */
public HashSet<Alergeno> getAlergenos() {
  return alergenos;
}

/**
 * Devuelve el precio establecido para el Producto
 * @return float precio del Producto
 */
public float getPrecio() {
  return precio;
}

/**
 * Devuelve el precio_real establecido para el Producto
 * @return float precio del Producto
 */
public float getPrecio_Real() {
  return precio_real;
}

/**
 * Devuelve la cantidad del Producto que está disponible
 * @return int con la cantidad de producto disponible
 */
public int getDisponibles() {
  return disponibles;
}

/**
 * Devuelve el tiempo de elaboración del producto (en minutos)
 * @return int tiempo de elaboración (en minutos)
 */
public int getTiempo_elaboracion() {
  return tiempo_elaboracion;
}

/******************
 * SETTERS
 ******************
 */

/**
 * Establece el ID del producto
 * @param id String con el ID del producto
 */
public void setId(String id) {
  this.id = id;
}

/**
 * Establece el nombre del producto
 * @param nombre String nombre del producto
 */
public void setNombre(String nombre) {
  this.nombre = nombre;
}

/**
 * Establece un listado de Alernos al Producto
 * @param alergenos Array[Alergenos] de Alérgenos
 */
public void setAlergeno(Alergeno[] alergenos) {
  this.alergenos = this.alergenos;
}


/**
 * Establece un HashSet de Alernos al Producto
 * @param alergenos HashSet<Alergeno>
 */
public void setAlergeno(HashSet<Alergeno> alergenos) {
  this.alergenos = this.alergenos;
}
/**
 * Establece un precio al Producto
 * @param precio float con el precio del producto
 */
public void setPrecio(float precio) {
  this.precio = precio;
}

/**
 * Establece un precio_real al Producto
 * @param precio float con el precio del producto
 */
public void setPrecio_real(float precio) {
  this.precio_real = precio;
}

/**
 * Establece las unidades disponibles del producto
 * @param disponibles int con las unidades disponibles del Producto
 */
public void setDisponibles(int disponibles) {
  this.disponibles = disponibles;
}

/**
 * Establece el tiempo de elaboración de un producto (en minutos)
 * @param tiempo_elaboracion int con los minutos de elaboración
 */
public void setTiempo_elaboracion(int tiempo_elaboracion) {
  this.tiempo_elaboracion = tiempo_elaboracion;
}


public void setContenido(ArrayList<Producto> prod){
  this.contenido=prod;
}

/******************
 * MÉDTODOS AUXILIARES
 ******************
 */
  
/**
 * Confirma si hay productos disponibles para poder ser servidos
 * @return Devuelve true si hay disponibles. Si la disponibilidad es 0 devuelve false.
 */
public boolean isDisponible(){
  return (this.disponibles!=0?true:false);
}

/**
 * Comprueba si un alérgeno se encuentra en el listado de alérgenos del producto
 * @param alergeno Alergeno a comprobar si está en el listado.
 * @return true si el alérgeno está en el listado y false si no
 */
public boolean hasAlergeno(Alergeno alergeno){
  return this.alergenos.contains(alergeno);
}

/**
 * Añade un alérgeno a la lista si este no se encuentra en el listado
 * @param alergeno Alergeno a eliminar
 */
public void addAlergeno(Alergeno alergeno){
  if (!this.hasAlergeno(alergeno)){
    this.alergenos.add(alergeno);
  }
}

/**
 * Elimina el alérgeno de la lista si este se encuentra en el listado
 * @param alergeno Alergeno a eliminar
 */
public void removeAlergeno(Alergeno alergeno){
  if (this.hasAlergeno(alergeno)){
    this.alergenos.remove(alergeno);
  }
}

@Override
public String toString(){
  StringBuilder builder = new StringBuilder();  
  builder.append("PRODUCTO: "+this.id+": "+this.nombre);
  builder.append("\n  precio: "+this.precio);
  builder.append("\n  precio real: "+this.precio_real);
  if(this.alergenos!=null){
    for(Alergeno ale: alergenos){
      builder.append("\n  Alérgenos:");
      builder.append("\n    --> "+ ale.getTipo());
    }
    
  }
  if(this.tiempo_elaboracion!=0){
    builder.append("\n  tiempo de elaboración: "+this.tiempo_elaboracion);
  }
  if(this.contenido!=null){
    builder.append("\n  Contenido:");
    for (Producto prod : contenido){
      builder.append("\n    --> "+ prod.getNombre());
    }
    
  }
  builder.append("\n  unidades disponible: "+this.disponibles);
  return builder.toString();  
}

}
