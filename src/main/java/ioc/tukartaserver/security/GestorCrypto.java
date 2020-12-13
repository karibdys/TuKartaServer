package ioc.tukartaserver.security;

import com.google.gson.Gson;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.MensajeSolicitud;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;


/**
 * Clase que se encarga de la criotografía y la seguridad 
 * @author Manu Mora
 */
public class GestorCrypto {

//datos necesarios
private static String KEY_STORE = "tukarta_keystore.ks";
private static String PASS_STORE = "tukartaPass";
private static String ALIAS_KEY = "tukarta";

//constructor de JSON
private final static Gson gson = new Gson();

//Parámetros del objeto
private KeyStore keyStore;
private SecretKey clavePublica;

/****************
 CONSTRUCTORES
****************/

/**
 * Constructor básico del gestor de seguridad y criptografía
 * @throws KeyStoreException si no se puede acceder al almacén de claves
 * @throws FileNotFoundException si el almacén de claves no existe
 * @throws IOException fallo general
 * @throws NoSuchAlgorithmException Si se indica un algoritmo erróneo
 * @throws CertificateException si se da un error en la obtención del certificado. 
 */
public GestorCrypto() throws KeyStoreException, IOException, FileNotFoundException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException{
  loadKeyStore();  
}

/**
 * Método que se encarga de iniciar el gestor de criptografía, obteniendo las claves necesarias para el funcionamiento. 
 * @throws KeyStoreException si no se puede acceder al almacén de claves
 * @throws FileNotFoundException si el almacén de claves no existe
 * @throws IOException fallo general
 * @throws NoSuchAlgorithmException Si se indica un algoritmo erróneo
 * @throws CertificateException si se da un error en la obtención del certificado. 
 */
public void loadKeyStore() throws KeyStoreException, FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
  keyStore= KeyStore.getInstance("JCEKS");
  File f = new File(KEY_STORE);
  if(f.isFile()){
    FileInputStream in = new FileInputStream(f);
    keyStore.load(in, PASS_STORE.toCharArray());     	 
  }else{
    log("Error al acceder al almacén de claves");
  }
  log("Almacén iniciado. Extrayendo claves");
  if (keyStore!=null){      
    /*SecretKey skey = keyGenerator(128);
    
    keyStore.setKeyEntry(ALIAS_KEY+3, skey.getEncoded(), keyStore.getCertificateChain(ALIAS_KEY));
    */
    clavePublica = (SecretKey)keyStore.getKey(ALIAS_KEY, PASS_STORE.toCharArray());  
  }  
}

/****************
 GETTERS
****************/

/**
 * Devuelve la clave pública del servidor
 * @return PublicKey con la clave del servidor. 
 */
public SecretKey getPublicKey (){
  return clavePublica;
}


/****************
 ENCRIPTADORES
****************/

/**
 * Encrypta un mensaje con clave simétrica a partir de un objeto MensajeRespuesta
 * @param mensaje MensajeRespuesta a encriptar
 * @return String en formato JSON listo para enviar al cliente/servidor
 */
public String encryptData (Mensaje mensaje){
   String dataJSON = gson.toJson(mensaje);
   //log("Mensaje sin encriptar: "+dataJSON);
   byte[] data = dataJSON.getBytes();
   byte[] encryptedData= null;
   try{
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, clavePublica);
      encryptedData = cipher.doFinal(data);
   }catch(Exception ex){
      System.err.println("Error cifrando los datos");
      System.err.println(ex.getMessage());
   }
   String JSONFinal = gson.toJson(encryptedData);
   //log("Mensaje encriptado: "+JSONFinal);
   return JSONFinal;
}

/**
 * Desencripta un mensaje con clave simétrica a partir de una cadena String en formato JSON
 * @param mensajeJSON String con el mensaje encriptado
 * @return MensajeRespuesta con el mensaje descifrado
 */
public Mensaje decryptData (String mensajeJSON, int tipoMens){
  byte[] data = gson.fromJson(mensajeJSON, byte[].class);
  byte[] originalData = null;
  try{
     Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
     cipher.init(Cipher.DECRYPT_MODE, clavePublica);
     originalData= cipher.doFinal(data);
  }catch(Exception ex){
     System.err.println("Error cifrando los datos");
     System.err.println(ex.getMessage());
  }
  String JSONFinal = new String(originalData);  
  MensajeRespuesta respuesta = null;
  MensajeSolicitud solicitud = null;
  switch (tipoMens){
    case Mensaje.MENSAJE_RESPUESTA:
      respuesta = gson.fromJson(JSONFinal, MensajeRespuesta.class);
      break;
    case Mensaje.MENSAJE_SOLICITUD:
      solicitud = gson.fromJson(JSONFinal, MensajeSolicitud.class);
      break;
  }
  
  return (tipoMens==Mensaje.MENSAJE_RESPUESTA)? respuesta:solicitud;
}

/****************
 PASS BASE DE DATOS
****************/

/**
 * Método que encripta una constraseña con el método HASH a partir 
 * @param pass
 * @param salt
 * @return
 * @throws NoSuchAlgorithmException 
 */
public static String encriptarPass(String pass, String salt) throws NoSuchAlgorithmException{
  String passFinal = pass+" "+salt;
  MessageDigest digestor = MessageDigest.getInstance("SHA-512"); 
  byte[] mensajeByte = passFinal.getBytes();
  byte[] mensajeHash= digestor.digest(mensajeByte);
  String mensajeDigest = Base64.getEncoder().encodeToString(mensajeHash);
  return mensajeDigest;    
}


/****************
 AUXILIARES
****************/

/**
 * Método auxiliar para mostrar parámetros por consola
 * @param texto String texto a mostrar
 */
public static void log(String texto){
  System.out.println("CRYPTO: "+texto);
}
  
}
