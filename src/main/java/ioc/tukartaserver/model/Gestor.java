package ioc.tukartaserver.model;

import java.util.ArrayList;

/**
 * Clase que extiende a Usuario y da forma a los gestores de restaurantes
 * @author Manu Mora
 */
public class Gestor extends Usuario{

private ArrayList<Restaurante> restaurantes;
private Rol rol;

/******************
 * CONSTRUCTOR
 ******************
 */

/**
 * Constructor básico de la clase Empleado.
 * @param nombreUser nombre de usuario del empleado
 * @param pass contraseña del empleado
 * @param email email del empleado
 * @param nombreReal nombre real del empleado
 * @param apellidoReal apellido real del empleado
 * @param restaurantes ArrayList de Restaurante que indica los restaurantes de los que es gestor
 */
public Gestor (String nombreUser, String pass, String email, String nombreReal, String apellidoReal, ArrayList<Restaurante> restaurantes){
  super(nombreUser, pass, email, nombreReal, apellidoReal, true);
  this.restaurantes=restaurantes; 
  this.rol = Rol.GESTOR;
}

/**
 * Constructor que emplea a un usuario ya creado para convertirlo en Empleado
 * @param user el usuario que se va a convertir en empleado
 * @param restaurantes ArrayList de Restaurante que indica los restaurantes de los que es gestor
 */
public Gestor (Usuario user, ArrayList<Restaurante> restaurantes){
  super(user.getUsuario(), user.getPwd(), user.getEmail(), user.getNombre(), user.getApellidos(), false);
  this.restaurantes=restaurantes; 
  this.rol = Rol.GESTOR;
  }  


/******************
 * GETTERS
 ******************
 */

/**
 * Devuelve la lista de restaurantes de la que el usuario es gestor
 * @return Restarante[]
 */
public ArrayList<Restaurante> getRestaurantes() {
  return restaurantes;
}

/******************
* SETTERS
******************
*/

/**
 * Establece el listado de restaurantes de los que el usuario es gestor
 * @param restaurantes 
 */
public void setRestaurantes(ArrayList<Restaurante> restaurantes) {
  this.restaurantes = restaurantes;
}

/******************
 * AUXILIARES
 ******************
 */

/**
 * Añade un restaurante al listado de restaurantes del Gestor y confirma si la operación se ha realizado con éxito o no.
 * @param res Restaurante
 * @return true si el restaurante se añade con éxito. False si no. 
 */
public boolean addRestaurante(Restaurante res){
  if (!this.restaurantes.contains(res)){
    this.restaurantes.add(res);
    return true;
  }else{
    return false;
  }  
}

/**
 * Elimina un restaurante del listado de restaurantes del Gestor y confirma si la operación se ha realizado con éxito o no.
 * @param res
 * @return true si el restaurante se elimina con éxito. False si no. 
 */
public boolean removeRestaurante(Restaurante res){
  if (this.restaurantes.contains(res)){
    this.restaurantes.remove(res);
    return true;
  }else{
    return false;
  }  
}

/**
 * Confirma si el Gestor está a cargo de un determinado restaurante
 * @param res Restaurante
 * @return true si el restaurante está en su listado y false si no
 */
public boolean hasRestaurante(Restaurante res){
  return this.restaurantes.contains(res);
}

  
}
