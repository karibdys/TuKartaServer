package ioc.tukartaserver.model;

/**
 * Clase que contiene los posibles códigos que puede ir en un MensajeRespuesta
 * @author Manu Mora
 */
public enum  Codigo {

//todo ok
CODIGO_OK("10","Petición realizada con éxito"),
//errores de input
CODIGO_DATOS_INCORRECTOS("40","Los datos enviados no son correctos o están incompletos"),
CODIGO_FUNCION_ERR("41", "La petición solicitada no está soportada por el sistema"),
CODIGO_NO_USER("42","No se ha indicado un mail de usuario"),
CODIGO_NO_PASS("43", "No se ha indicado una contraseña"),
CONDIGO_NO_SESION("44", "No hay sesión abierta vinculada a la petición"),
//error genérico
CODIGO_ERR("50", "Se ha producido un error al procesar la petición"),
//error de base de datos
CODIGO_ERR_BD ("60", "Error al acceder a la base de datos"),
CODIGO_ERR_USER("61", "El usuario no se encuentra en la base de datos o no tiene permisos para acceder a ella"),
CODIGO_ERR_PWD("62", "La constraseña introducida no es válida o no se corresponde con la del usuario"),
CODIGO_USER_REP("63", "El usuario ya se encuentra en la base de datos"),
//fin de conexión
END_CONNECTION("90", "Finalizando la conexión"),
END_SERVER("91", "Cerrando el servicio en el servidor");

private String code;
private String message;

private Codigo (String codigo, String mensaje){
  this.code=codigo;
  this.message=mensaje;
}

public String getCode(){
  return this.code;
}

public String getMessage(){
  return this.message;
}

public String toString(){
  return "  Codigo: "+this.getCode()+" \n  Mensaje: "+this.getMessage();
}


}
