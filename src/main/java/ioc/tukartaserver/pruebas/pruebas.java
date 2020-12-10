/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.pruebas;

import com.google.gson.Gson;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.Utiles;
import ioc.tukartaserver.security.GestorCrypto;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;



/**
 *
 * @author Manu
 */
public class pruebas {

public static final String LIST_PEDIDO_FROM_USER = "SELECT* from PEDIDO WHERE empleado = ? AND activo = true";
public static Gson gson = new Gson();


public static void main (String... args)  {  
  String comprobar = "2r0ZCHoK/nVyMniIr2L3kXklx0X+hHO5+vLneaG36x+ucY+m0nzjMpNbqvWc722axohnsuZWQc8/tfbfyUYMfA==";
  String pass = "marcPass";
  String salt = "marc@tukarta.com";
  String mensajeFinal ="";
  try {
    mensajeFinal = GestorCrypto.encriptarPass(pass, salt);
  } catch (NoSuchAlgorithmException ex) {
    log("ERROR");
  }
  log(mensajeFinal);
  log(comprobar.equals(mensajeFinal)? "WAI": "NO WAI");
    

}

public static void log(String text){
  System.out.println(text);
}

}
