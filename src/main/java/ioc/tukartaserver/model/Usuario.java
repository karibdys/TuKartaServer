package ioc.tukartaserver.model;

import java.util.Date;
import org.json.JSONObject;

/**
 * Clase que representa un usuario de la base de datos del proyecto TuKarta
 * @author Manu Mora
 */
public class Usuario {

private String nomUsuario;
private String password;
private String email;
private String nombre;
private String apellido;
//private Restaurante[] restaurantes;
private Date fechaAlta;
private Date fechaUltimaMod;
private Date fechaBaja;

//nombres de los campos
public static final String NOM_USUARIO = "usuario";
public static final String NOM_REAL = "nombre";
public static final String APE_REAL = "apellidos";
public static final String PASSWORD = "password";
public static final String EMAIL = "email";
public static final String FECHA_ALTA = "fecha_alta";
public static final String FECHA_MOD = "fecha_modificacion";
public static final String FECHA_BAJA = "fecha_baja";

/**
 * Constructor básico de un usuario sin parámetros. Incluye la fecha de creación.
 */
public Usuario(){
 this.fechaAlta= this.fechaUltimaMod = new Date();    
}
/**
 * Constructor de la clase Usuario
 * @param nombreUser 
 * @param pass
 * @param email
 * @param nombreReal
 * @param apellidoReal 
 */
public Usuario (String nombreUser, String pass, String email, String nombreReal, String apellidoReal){
  this.nomUsuario=nombreUser;
  this.password=pass;
  this.email=email;
  this.nombre=nombreReal;
  this.apellido=apellidoReal;
  this.fechaAlta= this.fechaUltimaMod = new Date();  
}

public Usuario(JSONObject json){
  if (json.has(NOM_USUARIO)){
    this.nomUsuario=(String) json.get(NOM_USUARIO);
  };
  if (json.has(PASSWORD)){
    this.password=(String) json.get(PASSWORD);
  };
  if (json.has(EMAIL)){
    this.email=(String) json.get(EMAIL);
  };
  if (json.has(NOM_REAL)){
    this.nombre=(String) json.get(NOM_REAL);
  };
  if (json.has(APE_REAL)){
    this.apellido=(String) json.get(APE_REAL);
  };
  if (json.has(FECHA_ALTA)){
    this.fechaAlta= Utiles.ParseDate(json.getString(FECHA_ALTA));
  };
  if (json.has(FECHA_BAJA)){
    this.fechaBaja= Utiles.ParseDate(json.getString(FECHA_BAJA));
  };
  if (json.has(FECHA_MOD)){
    this.fechaUltimaMod= Utiles.ParseDate(json.getString(FECHA_MOD));
  };
}

  public String getNomUsuario() {
    return nomUsuario;
  }

  public String getPassword() {
    return password;
  }

  public String getEmail() {
    return email;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  /*
  public Restaurante[] getRestaurantes() {
    return restaurantes;
  }
  */

  public Date getFechaAlta() {
    return fechaAlta;
  }

  public Date getFechaUltimaMod() {
    return fechaUltimaMod;
  }

  public Date getFechaBaja() {
    return fechaBaja;
  }

  public void setNomUsuario(String nomUsuario) {
    this.nomUsuario = nomUsuario;
    this.fechaUltimaMod= new Date();
  }

  public void setPassword(String password) {
    this.password = password;
    this.fechaUltimaMod= new Date();
  }

  public void setEmail(String email) {
    this.email = email;
    this.fechaUltimaMod= new Date();
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
    this.fechaUltimaMod= new Date();
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
    this.fechaUltimaMod= new Date();
  }
  
  /*
  public void setRestaurantes(Restaurante[] restaurantes) {
    this.restaurantes = restaurantes;
    this.fechaUltimaMod= new Date();
  }
  */

  public void setFechaUltimaMod(Date fechaUltimaMod) {
    this.fechaUltimaMod = fechaUltimaMod;    
  }

  public void setFechaBaja(Date fechaBaja) {
    this.fechaBaja = fechaBaja;
    this.fechaUltimaMod= new Date();
  }
  
  /**
   * Método que convierte este usuario en un objeto JSONObject para poder trabajar con él como si fuese un JSON
   * 
   * @return un objeto JSONObject con todos los parámetros del usuario.
   */
  public JSONObject parseJSON(){
  JSONObject json = new JSONObject();
  json.put(NOM_USUARIO, this.nomUsuario);
  json.put(EMAIL, this.email);
  json.put(PASSWORD, this.password);
  json.put(NOM_REAL, this.nombre);
  json.put(APE_REAL, this.apellido);      
  json.put(FECHA_ALTA, Utiles.ParseDate(this.fechaAlta));
  json.put(FECHA_MOD, Utiles.ParseDate(this.fechaUltimaMod));
  if (this.fechaBaja!=null){
    json.put(FECHA_BAJA, Utiles.ParseDate(this.fechaBaja));
  }    
  return json;
}  
  
  @Override
  public String toString(){
    StringBuilder builder =new StringBuilder();
    builder.append("usuario: "+this.nomUsuario);
    builder.append("\npassword: "+this.password);
    builder.append("\nnombre real: "+this.nombre);
    builder.append("\napellido real: "+this.apellido);
    builder.append("\nemail: "+this.email);
    
    return builder.toString();
  }
  
}
