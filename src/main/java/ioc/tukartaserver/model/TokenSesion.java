package ioc.tukartaserver.model;

import ioc.tukartaserver.model.Usuario;
import java.time.LocalTime;
import java.util.Random;

/**
 *
 * @author Manu
 */
public class TokenSesion {

//minutos válidos que tendrá la sesión como máximo
private static final long MIN_MAX = 30;

private String token;
private String usuario;
private LocalTime validez;

public TokenSesion (Usuario user){
  this.usuario=user.getUsuario();
  this.token=generateToken();
  //this.validez= validezHasta(MIN_MAX);
  this.validez=validezHasta(MIN_MAX);
}

public void setToken(String token) {
  this.token = token;
}

public void setUsuario(String usuario) {
  this.usuario = usuario;
}

public void setValidez(LocalTime validez) {
  this.validez = validez;
}

public String getToken(){
  return this.token;
}

public String getUsuario(){
  return this.usuario;
}

public LocalTime getValidez(){
  return this.validez;
}

private static String generateToken(){
  StringBuilder token=new StringBuilder(10);
  
  char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  Random random = new Random();
  for (int i = 0; i < 10; i++) {
    char c = chars[random.nextInt(chars.length)];
    token.append(c);
  }
  
  return token.toString();
}

public static LocalTime validezHasta(long minutos){
  LocalTime hora= LocalTime.now().plusMinutes(minutos);
  return hora;
}

public String toString(){
  StringBuilder builder = new StringBuilder();
  builder.append("Token: "+this.token);
  builder.append("\nUser: "+this.usuario);
  builder.append("\nValidez: "+this.validez);
  return builder.toString();
}

}
