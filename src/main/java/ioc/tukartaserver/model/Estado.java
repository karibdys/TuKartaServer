package ioc.tukartaserver.model;

/**
 * 
 * @author Manu Mora
 */
public enum Estado {
  NO_INICIADO ("pendiente"),
  EN_PREPARACIÓN ("preparando"),
  PREPARADO ("listo");
  
  private String estado;
  private Estado (String estado){
    this.estado = estado;
  }
  
  public String getEstado(){
    return this.estado;
  }
  
}
