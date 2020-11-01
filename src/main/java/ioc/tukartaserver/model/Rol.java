package ioc.tukartaserver.model;

/**
 * Representa los tipos de rol posibles dentro de un Usuario
 * @author Manu Mora
 */
public enum Rol { 
  CAMARERO("camarero"), COCINERO("cocinero"), GESTOR("gestor");
  
  private String nombreRol;
  
 /******************
 * CONSTRUCTOR
 ******************
 */
  
  /**
   * Constructor de la clase. Construye un rol a partir de un tipo indicado en las constantes de la clase. 
   * @param tipo String tipo de rol.
   */
  private Rol (String tipo){
    this.nombreRol=tipo; 
  }
  
 /******************
 * GETTERS
 ******************
 */
  
  /**
   * Devuelve el nombre del rol en formato String
   * @return String
   */
  public String getNombreRol(){
    return this.nombreRol;
  }
  
  /**
   * Devuelve un String con la representación en caracteres de un rol. 
   * @return 
   */
  @Override
  public String toString(){
    return getNombreRol();
  }
  
}
