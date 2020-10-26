package ioc.tukartaserver.model;

import com.google.gson.Gson;

/**
 * Clase que representa un mensaje de respuesta del cliente o del servidor al otro. 
 * @author Manu Mora y David Domenech
 */
public class MensajeRespuesta implements Mensaje{
private static Gson gson = new Gson();

private Codes code;
private String peticion;
private String data;
private String dataUser;


/**
 * Constructor básico de un MensajeRespuesta
 */
public MensajeRespuesta(){  
}

public MensajeRespuesta(Codes code, String peticion){
  this.code=code;
  this.peticion=peticion;
}

public MensajeRespuesta(Codes code, String peticion, String data){
  this.code=code;
  this.peticion=peticion;
  this.data=data;  
}

public MensajeRespuesta(Codes code, String peticion, Object data){
  gson = new Gson();
  this.code=code;
  this.peticion=peticion;
  this.data = gson.toJson(data); 
}

public MensajeRespuesta(Codes code, String peticion, String data, String dataUser){
  this.code=code;
  this.peticion=peticion;
  this.data=data;
  this.dataUser=dataUser;
}

public MensajeRespuesta(Codes code, String peticion, Object data, Usuario user){
  gson = new Gson();
  this.code=code;
  this.peticion=peticion;
  this.data = gson.toJson(data);
  this.dataUser=gson.toJson(user);
}



  public Codes getCode() {
    return code;
  }

  public String getPeticion() {
    return peticion;
  }

  public String getData() {
    return data;
  }

  public void setCode(Codes code) {
    this.code = code;
  }

  public void setPeticion(String peticion) {
    this.peticion = peticion;
  }

  public void setData(String data) {
    this.data = data;
  }
    
  public void setData (Object data){
    this.data = gson.toJson(data);
  }
  
  public void setDataUser(String dataUser) {
    this.dataUser = dataUser;
  }
  
  public void setDataUser (Usuario user){
    this.data = gson.toJson(user);
  }
    
  @Override
  public String toString(){
    StringBuilder builder = new StringBuilder();
    builder.append("------\nMENSAJE:");
    builder.append("\nCódigo: "+code.getCode()+"\nMensaje: "+code.getMessage());
    builder.append("\nPetición respondida: "+peticion);
    builder.append("\nDatos extras: "+data);
    if(dataUser!=null){
      builder.append("\nUsuario: "+dataUser);
    }    
    builder.append("\n------");
    return builder.toString();
  }
}
