package ioc.tukartaserver.model;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Clase que se encarga de gestionar los Menús, que son subclases de Producto y que pueden contener,a su vez, varios productos y tener un precio distinto. 
 * @author Manu
 */
public class Menu extends Producto {

private ArrayList<Producto> contenido;
private float precio_real;


/******************
 * CONSTRUCTOR
 ******************
 */

/**
 * COnstructor de un Menu
 * @param id
 * @param nombreMenu 
 */
public Menu (String id, String nombreMenu){
  this.id=id;
  this.nombre=nombreMenu; 
  this.precio_real = 0;
}

/******************
 * GETTER
 ******************
 */

public ArrayList<Producto> getContenido() {
  return contenido;
}

public float getPrecio_real() {
  return precio_real;
}

  
/******************
 * SETTER
 ******************
 */
public void setContenido(ArrayList<Producto> contenido) {
  this.contenido = contenido;
}

public void setPrecio_real(float precio_real) {
  this.precio_real = precio_real;
}



/******************
 * MÉTODOS AUXILIARES
 ******************
 */

public void addProducto(Producto prod){  
  //añade el producto
  this.contenido.add(prod);
  //comprueba los alérgenos que hay en él y los añade si es necesario
  for (Alergeno alerg : prod.getAlergenos()){
    this.addAlergeno(alerg);
  }  
  //recalcula el precio del producto final
  calcularPrecio();
}

public void removeProducto(int posicionProd){
  //obtenemos el producto
  Producto prod = contenido.get(posicionProd);
  //elinimanos el producto
  contenido.remove(posicionProd);
  //comprobamos qué alérgenos quedan  
}
  
/**
 * Calcula el precio real del total de productos que se han introducido en un Menú sumando sus precios. 
 */
public void calcularPrecio(){
  this.precio_real=0;
  for (Producto prod: contenido){
    this.precio_real+=prod.getPrecio();
  }
}  

public void actualizarAlergenos(){
  //limpiamos el set de alérgenos
  this.alergenos.clear();
  for (Producto prod : contenido){
    HashSet<Alergeno> lAlerg = prod.getAlergenos();
    for (Alergeno alerg : lAlerg){
      //añadimos los alérgenos de cada uno de los productos (si no están repetidos)
      this.alergenos.add(alerg);
    }    
  }
}
}
