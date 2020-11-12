package ioc.tukartaserver.model;

import java.util.Date;
import java.util.HashSet;

/**
 * Subclase de la clase Usuario que controla los datos de un empleado de un restaurante. 
 * @author Manu Mora
 */
public class Empleado extends Usuario{

private Usuario gestor;
private Restaurante trabajadorDe;
private float salario;
private HashSet<Pedido> pedidos;
private Rol rol;

private Boolean isActive;

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
 * @param gestor gestor que lo contrata
 * @param rol rol en el que trabaja el empleado
 */
public Empleado (String nombreUser, String pass, String email, String nombreReal, String apellidoReal, Usuario gestor, Rol rol){
  
  super(nombreUser, pass, email, nombreReal, apellidoReal, false);
  this.gestor=gestor;
  this.rol= rol;
}

/**
 * Constructor que emplea a un usuario ya creado para convertirlo en Empleado
 * @param user el usuario que se va a convertir en empleado
 * @param gestor el gestor que lo contrarta
 * @param rol el rol con el que lo contrata
 */
public Empleado (Usuario user, Usuario gestor, Rol rol){
  super(user.getUsuario(), user.getPwd(), user.getEmail(), user.getNombre(), user.getApellidos(), false);
  this.gestor=gestor;
  this.rol=rol;
  }

/**
 * Crea un empleado a partir de un Usuario
 * @param user Usuario a partir del que se crea el Empleado
 */
public Empleado (Usuario user){
  super(user.getUsuario(), user.getPwd(), user.getEmail(), user.getNombre(), user.getApellidos(), false);
}

public Empleado() {
}

public Empleado (String nombreUser, String pass, String email, String nombreReal, String apellidoReal, Date fechaAlta, Date fechaMod, Date fechaBaja, boolean isGestor){
  super (nombreUser, pass, email, nombreReal, apellidoReal, fechaAlta, fechaMod, fechaBaja, isGestor);  
}

/******************
 * SETTERS
 ******************
 */
/**
 * Establece el gestor que emplea al usuario
 * @param gestor Gestor que contrata al usuario
 */
public void setGestor(Usuario gestor) {
  this.gestor = gestor;
}

/**
 * Establece el salario de un empleado
 * @param salario float con el salario del empleado
 */
public void setSalario(float salario) {
  this.salario = salario;
}

/**
 * Establece el rol de un empleado
 * @param rol Rol del empleado
 */
public void setRol(Rol rol) {
  this.rol = rol;
}

/**
 * Establece el restaurante para el que trabaja el empleado
 * @param res Restaurante en el que trabaja el empleado
 */
public void setRestaurante(Restaurante res){
  this.trabajadorDe=res;
}

/******************
 * GETTERS
 ******************
 */

/**
 * Devuelve el gestor que emplea al empleado
 * @return Gestor gestor que emplea al empleado
 */
public Usuario getGestor() {
  return gestor;
}

/**
 * Devuelve el salario del empleado
 * @return float con el salario del empleado
 */
public float getSalario() {
  return salario;
}

/**
 * Devuelve el rol del empleado
 * @return Rol del empleado
 */
public Rol getRol() {
  return rol;
}

/**
 * Devuelve el restaurante en el que trabaja el empleado
 * @return Restaurante 
 */
public Restaurante getTrabajadorDe(){
  return this.trabajadorDe;
}

/******************
 * AUXILIARES
 ******************
 */
  
/**
 * Establece a un empleado activo. Necesita el gestor que le da de alta.   
 * @param gestor El gestor que le da de alta y lo contrata
 */
public void setActive(Gestor gestor){
  this.isActive=true;
  this.gestor=gestor;
}

/**
 * Establece a un empleado inactivo. Elimina sus relaciones con el gestor, el restaurante, su rol y su salario. 
 */
public void setInactive(){   
  this.isActive=false;
  this.salario=0;
  this.gestor=null;
  this.trabajadorDe=null;    
  this.rol=null;
}
  
/**
 * Comprueba que un empleado está en activo o no.
 * @return Devuelve true si el empleado tiene salario, y gestor que lo contrata. Si no, devuelve false. 
 */
public boolean isActive(){
  if(salario==0 || gestor== null){
    isActive=false;
    return isActive;
  }else{
    isActive=true;
    return isActive;
  }
}
  
/**
 * Añade el pedido al usuario si este no estaba añadido ya
 * @param ped Pedido a añadir al listado
 */
public void addPedido(Pedido ped){
  pedidos.add(ped);
}

/**
 * Elimina el pedido indicado si existe
 * @param ped Pedido a eliminar
 * @return devuelve true si el pedido se elimina y false si no
 */
public boolean removepedido(Pedido ped){  
  if (pedidos.contains(ped)){
    pedidos.remove(ped);
    return true;
  }else{
    return false;
  }
  
}

/**
 * Cambia un pedido por otro o por el mismo pero actualizado (si existe)
 * @param pedInicial Pedido a actualizar
 * @param pedFinal Pedido actualizado
 */
public void updatePedido(Pedido pedInicial, Pedido pedFinal){
  if (pedidos.contains(pedInicial)){
    pedidos.remove(pedInicial);
    pedidos.add(pedFinal);
  }   
}

/**
 * Devuelve un Strin con la representación en texto de este usuario.
 * @return String
*/
@Override
public String toString(){
  StringBuilder builder = new StringBuilder();
  builder.append(super.toString());
  if (this.gestor!=null){
    builder.append("\ngestor contratante: "+this.gestor.getNombre());
  }
  if (this.rol!=null){
    builder.append("\nrol: "+this.rol);
  }  
  if (salario!=0){
    builder.append("\nsalario: "+salario+"€");
  }
  return builder.toString();
}          


}
