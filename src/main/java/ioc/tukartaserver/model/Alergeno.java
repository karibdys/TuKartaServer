package ioc.tukartaserver.model;

/**
 * Clase con los distintos alérgenos que se pueden encontrar en los productos y menús
 * @author Manu
 */
public enum Alergeno {
  PESCADO ("pescado"), 
  FRUTOS_SECOS ("frutos_secos"), 
  LACTEOS ("lacteos"), 
  MOLUSCOS ("moluscos"), 
  GLUTEN("gluten"),
  CRUSTACEO("crustaceos"),
  HUEVO("huevo"),
  CACAHUETE("cacahuete"),
  SOJA("soja"),
  APIO("apio"),
  MOSTAZA("mostaza"),
  SESANO("sesamo"),
  ALTRAMUZ("altramuz"),
  SULFITO("sulfito");

private String tipo;


/******************
 * CONSTRUCTOR
 ******************
 */

/**
 * Constructor de la clase. Recibe un String que es el nombre del tipo de alérgeno.
 * @param tipo String nombre del tipo o categoría de alérgeno
 */
private Alergeno (String tipo){
  this.tipo = tipo;
}
  
/**
 * Devuelve el nombre del alérgeno en cuestión
 * @return String con el nombre del tipo o categoría de alérgeno
 */
public String getTipo(){
  return this.tipo;
}
}
