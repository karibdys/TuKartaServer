package ioc.tukartaserver.security;

import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import ioc.tukartaserver.model.MensajeSolicitud;
import ioc.tukartaserver.model.Utiles;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * CLase para comprobar que la clase funciona correctamente. 
 * @author Manu Mora
 */
public class GestorCryptoTest {


private static String KEY_STORE_CORRECTA = "tukarta_keystore.ks";
private static String PASS_STORE_CORRECTA = "tukartaPass";
private static String ALIAS_KEY_CORRECTA = "tukarta";

public GestorCryptoTest() {
}

@BeforeClass
public static void setUpClass() {
}

@AfterClass
public static void tearDownClass() {
}

@Before
public void setUp() {
}

@After
public void tearDown() {
}

/********************
 * LOAD KEY STORE
********************/

 /**
 * Método para comprobar que el método loadKeyStore. Como todos los datos internos son los que son y no se pueden modificar, solo podemos comprobar que no da fallos al iniciarse.
 * @throws Exception 
 */
  @Test
  public void testLoadKeyStore_fileNoValid() throws Exception {
    System.out.println("loadKeyStore");
    GestorCrypto gestor = new GestorCrypto();
    gestor.loadKeyStore();
    if (gestor.getPublicKey()!=null){
      assert(true);
    }else{
      assert(false);
    }
  }

/********************
 * ENCRYPDATA
********************/
  /**
   * Prueba del método encrypdata con un Mensaje de tipo Respuesta
  */
  @Test
  public void testEncryptDataMResp() {
  try {
    GestorCrypto gestor = new GestorCrypto();
    MensajeRespuesta mensaje = Utiles.mensajeOK("prueba encrypdata");    
    String result = gestor.encryptData(mensaje);
    
    //desencriptamos el mensaje y comprobamos que es igual al que teníamos al principio
    MensajeRespuesta expResult = (MensajeRespuesta) gestor.decryptData(result, Mensaje.MENSAJE_RESPUESTA);
    
    if (mensaje.toString().equals(expResult.toString())){
      assert(true);
    }else{
      assert(false);
    }
    
  } catch (Exception ex) {
    assert(false);
  } 
  }
  
  /**
   * Prueba del método encrypdata con un Mensaje de tipo Solicitud
  */
  @Test
  public void testEncryptData_MSol() {
  try {
    GestorCrypto gestor = new GestorCrypto();
    MensajeSolicitud mensaje = new MensajeSolicitud();
    mensaje.setPeticion("prueba encrypdata");
    mensaje.setData("Un dato cualquiera");
    String result = gestor.encryptData(mensaje);
    
    //desencriptamos el mensaje y comprobamos que es igual al que teníamos al principio
    MensajeSolicitud expResult = (MensajeSolicitud) gestor.decryptData(result, Mensaje.MENSAJE_SOLICITUD); 
    if (mensaje.toString().equals(expResult.toString())){
      assert(true);
    }else{
      assert(false);
    }
    
  } catch (Exception ex) {
    assert(false);
  } 
  }
  
/********************
 * DECRYPDATA
********************/
  /**
   * Prueba del método decrypdata con los datos correctos (el mensaje pasado como parámetro es del mismo tipo que el segundo parámetro)
   */
  @Test
  public void testDecryptData_OK() {
   try {
    GestorCrypto gestor = new GestorCrypto();
    MensajeRespuesta mensaje = Utiles.mensajeOK("prueba decrypdata");    
    String result = gestor.encryptData(mensaje);
    
    //desencriptamos el mensaje y comprobamos que es igual al que teníamos al principio
    MensajeRespuesta expResult = (MensajeRespuesta) gestor.decryptData(result, Mensaje.MENSAJE_RESPUESTA);
    
    if (mensaje.toString().equals(expResult.toString())){
      assert(true);
    }else{
      assert(false);
    }
    
  } catch (Exception ex) {
    assert(false);
  } 
  }

  /**
   * Prueba del método decrypdata con un mensaje de un tipo y un parámetro de otro
   */
  @Test
  public void testDecryptData_NoOK() {
   try {
    GestorCrypto gestor = new GestorCrypto();
    MensajeRespuesta mensaje = Utiles.mensajeOK("prueba decrypdata");    
    String result = gestor.encryptData(mensaje);
    
    //desencriptamos el mensaje y comprobamos que da fallo
    MensajeRespuesta expResult = (MensajeRespuesta) gestor.decryptData(result, Mensaje.MENSAJE_SOLICITUD);
    
    assert(false);
    
  } catch (Exception ex) {
    assert(true);
  } 
  }
  
/********************
 * ENCRIPTAR PASS
********************/
  /**
   * Comprueba que el método de encriptar la pass funciona correctamente
   * @throws Exception 
   */
  @Test
  public void testEncriptarPass() throws Exception {
    String pass = "passPrueba";
    String salt = "email@email.com";
    String expResult = "/QdAldVWIsSbmYVM5sCgyA7fqhtIfhsZTTtQ5fUSo+0zBPK5kUkFsxB45ohqjfO4zaBjR8Yel8qiXuY3iO6dCw==";
    String result = GestorCrypto.encriptarPass(pass, salt);
    assertEquals(expResult, result);
  }
  
  /**
   * Comprueba que el método de encriptar la pass no da el mismo resultado cuando se cambian los datos
   * @throws Exception 
   */
  @Test
  public void testEncriptarPass_NoRepeat() throws Exception {
    String pass = "passPrueba";
    String salt = "email@email.com";
    String resultado_A = GestorCrypto.encriptarPass(pass, salt);
    pass = "passPrueba2";
    String resultado_B = GestorCrypto.encriptarPass(pass, salt);
    
    if(resultado_A.equals(resultado_B)){
      assert(false);      
    }else{
      salt = "email2@email.com";
      String resultado_C = GestorCrypto.encriptarPass(pass, salt);
      if (resultado_A.equals(resultado_C)){
        assert(false);
      } else{
        assert(true);
      }
    }    
    
  }

}
