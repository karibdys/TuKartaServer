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
  SecretKey skey=null;
  GestorCrypto crypto =null;
  try {
    crypto = new GestorCrypto();
    skey = crypto.getPublicKey();
    
  } catch (KeyStoreException ex) {
    Logger.getLogger(pruebas.class.getName()).log(Level.SEVERE, null, ex);
  } catch (IOException ex) {
    Logger.getLogger(pruebas.class.getName()).log(Level.SEVERE, null, ex);
  } catch (NoSuchAlgorithmException ex) {
    Logger.getLogger(pruebas.class.getName()).log(Level.SEVERE, null, ex);
  } catch (CertificateException ex) {
    Logger.getLogger(pruebas.class.getName()).log(Level.SEVERE, null, ex);
  } catch (UnrecoverableKeyException ex) {
    Logger.getLogger(pruebas.class.getName()).log(Level.SEVERE, null, ex);
  }
  if (skey!=null){
    log("tenemos clave");
    MensajeRespuesta res = Utiles.mensajeOK("pruebas");
    res.setData("WAI");
    String dataToSend =crypto.encryptData(res);
    log(dataToSend);
    
    MensajeRespuesta res2 = crypto.decryptData(dataToSend);
    log(res2.toString());
    
  }
}

public static void log(String text){
  System.out.println(text);
}

public static SecretKey keyGenerator(int size){
   SecretKey skey = null;
  
   if ((size==128)||(size==192)||(size==256)){
       try{
         //instanciamos el generador con el algoritmo
         KeyGenerator kgen = KeyGenerator.getInstance("AES");
         //iniciamos el generador con el tamaño de la clave
         kgen.init(size);
         //creamos la clave
         skey=kgen.generateKey();
      }catch (NoSuchAlgorithmException ex){
         System.err.println("Generador no disponible");
      }
   }
   return skey;
}

  
}
