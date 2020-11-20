package ioc.tukartaserver.model;

/**
 * 
 * @author Manu Mora
 */
public enum Estado {
  PENDIENTE ("pendiente"),
  PREPARANDO ("preparando"),
  LISTO ("listo");
  
  private String estado;
  private Estado (String estado){
    this.estado = estado;
  }
  
  public String getEstado(){
    return this.estado;
  }
  
}
