package ioc.tukartaserver.model;
import java.util.ArrayList;


/**
 * Modelo que representa un Restaurante en la aplicación TuKarta
 * @author Manu Mora
 */
public class Restaurante {

private String id;
private String nombre;
private ArrayList<Mesa> mesas;
private Provincia provincia;
private String direccion;

/******************
 * CONSTRUCTOR
 ******************
 */

public Restaurante(){
  
}
/**
 * Constructor básico para un Restaurante. Necesita un ID y un nombre
 * @param id String ID del restaurante
 * @param nombre String nombre del Restaurante
 */
public Restaurante(String id, String nombre) {
  this.id = id;
  this.nombre = nombre;
}

/**
 * Constructor completo de un Restaurante. 
 * @param id String ID del restaurante
 * @param nombre String nombre del restaurante
 * @param prov Provincia del restaurante
 * @param direccion String con la dirección del restaurante
 */
public Restaurante (String id, String nombre, Provincia prov, String direccion){
  this.id = id;
  this.nombre = nombre;
  this.provincia=prov;
  this.direccion=direccion;
}


/******************
 * GETTERS
 ******************
 */

/**
 * Devuelve el ID de un Restaurante
 * @return String id del Restaurante
 */
  public String getId() {
    return id;
  }

  /**
   * Devuelve el nombre del Restaurante
   * @return String con el nombre del Restaurante
   */
  public String getNombre() {
    return nombre;
  }
  
  /**
  * Devuelve el listado de objetos Mesa del Restaurante
  * @return HashSet<Mesa> 
  */
  public ArrayList<Mesa> getMesas() {
    return mesas;
  }

  /**
   * Devuelve la provincia del Restaurante
   * @return Provincia provincia del restaurante
   */
  public Provincia getProvincia() {
    return provincia;
  }
  
  /**
   * Devuelve la dirección del Restaraunte
   * @return String con al dirección del restaurante
   */
  public String getDireccion() {
    return direccion;
  }


 /******************
  * SETTERS
  ******************
 */

  /**
   * Establece un ID al Restaurante
   * @param id String con el ID
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Establece un nombre al Restaurante
   * @param nombre String con el nombre del restaurante
   */
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   * Establece un listado de objetos Mesa al restaurante
   * @param mesas HashSet<Mesa>
   */
  public void setMesas(ArrayList<Mesa> mesas) {
    this.mesas = mesas;
  }

  /**
   * Establede una Provincia al Restaurante
   * @param provincia Provincia a establecer
   */
  public void setProvincia(Provincia provincia) {
    this.provincia = provincia;
  }

  /**
   * Establece una dirección al Restaurante
   * @param direccion String dirección
   */
  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }
  

/******************
 * MÉTODOS AUXILIARES
 ******************
 */
  
  /**
   * Añade una Mesa al listado de mesas si esta no está ya añadida
   * @param mesa Mesa a añadir
   */
  public void addMesa(Mesa mesa){
    mesas.add(mesa);
  }
  
  /**
   * Elimina una Mesa del listado de mesas si la mesa ya estaba en el listado
   * @param mesa Mesa a eliminar
   * @return true si la mesa se elimina con éxito, false si la mesa no está en el listado
   */
  public boolean removeMesa(Mesa mesa){
    if (mesas.contains(mesa)){
      mesas.remove(mesa);
      return true;
    }else{
      return false;
    }
  }
}
