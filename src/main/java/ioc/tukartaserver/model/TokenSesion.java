package ioc.tukartaserver.model;

import ioc.tukartaserver.model.Usuario;
import java.util.Random;

/**
 *
 * @author Manu
 */
public class TokenSesion {
private String token;
private String usuario;

public TokenSesion (Usuario user){
  this.usuario=user.getUsuario();
  this.token=generateToken();
}

private String generateToken(){
  StringBuilder token=new StringBuilder(10);
  
  char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  Random random = new Random();
  for (int i = 0; i < 10; i++) {
    char c = chars[random.nextInt(chars.length)];
    token.append(c);
  }
  
  return token.toString();
}

public String getToken(){
  return this.token;
}

public String getUsuario(){
  return this.usuario;
}
  
}
