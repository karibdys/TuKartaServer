package ioc.tukartaserver.gestorDB;

import ioc.tukartaserver.model.Codes;
import ioc.tukartaserver.model.Mensaje;
import ioc.tukartaserver.model.MensajeRespuesta;
import java.sql.SQLException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Clase que controla el funcionamiento del gestor de la base de datos. 
 * @author Manu Mora
 */
public class GestorDBTest {
private static GestorDB gestor;
private static String userCorrecto="manu@tukarta.com";
private static String userCorrectoAdmin="marc@tukarta.com";
private static String userIncorrecto="luis@tukarta.com";
private static String passCorrecta="manuPass";
private static String passCorrectaAdmin="marcPass";
private static String passIncorrecta="luisPass";

public GestorDBTest() {
}

@BeforeClass
public static void setUpClass() throws SQLException, ClassNotFoundException {
  gestor = new GestorDB(); 
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

  /*  
  *************
   LOGIN
  ***************
  */

  /**
   * Comprueba que el método devuelve un mensaje con código 40 en caso de recibir datos nulos.
   * La petición que recibe es de tipo LOGIN normal. 
   */
  @Test
  public void datosNullLogin(){
    String userNull=null;
    String passNull=null;    
    String user="userCualquiera";
    String pass="passCualquiera";
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;
    //primero comprobamos que devuelve un código 40 si el usuario es nulo y la pass no
    //también comprobamos que la petición recibida es de LOGIN
    MensajeRespuesta res =gestor.loginMens(userNull, pass, false);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (codeResult.equals(expResult) && petResult.equals(Mensaje.FUNCION_LOGIN)){
      //si se cumple el primero,
      //comprobamos que devuelve un código 40 si el usuario no es nulo y la pass sí
      codeResult = gestor.loginMens(user, passNull, false).getCode().getCode();            
    }
    //comprobamos que se cumple el segundo requisito.
    assertEquals(expResult, codeResult);
  }
  
  /**
   * Comprueba que el método devuelve un mensaje con código 40 en caso de recibir datos nulos.
   * La petición que recibe es de tipo LOGIN ADMIN. 
   */
  @Test
  public void datosNullLoginAdmin(){
    String userNull=null;
    String passNull=null;    
    String user="userCualquiera";
    String pass="passCualquiera";
    String expResult = Codes.CODIGO_DATOS_INCORRECTOS;
    //primero comprobamos que devuelve un código 40 si el usuario es nulo y la pass no
    //también comprobamos que la petición recibida es de LOGIN_ADMIN
    MensajeRespuesta res =gestor.loginMens(userNull, pass, true);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (codeResult.equals(expResult) && petResult.equals(Mensaje.FUNCION_LOGIN_ADMIN)){
      //si se cumple el primero,
      //comprobamos que devuelve un código 40 si el usuario no es nulo y la pass sí
      codeResult = gestor.loginMens(user, passNull, true).getCode().getCode();            
    }
    //comprobamos que se cumple el segundo requisito.
    assertEquals(expResult, codeResult);
  }
  
  /**
   * Comprueba que devuelve correctamente un código 61 cuando un usuario no se encuentra en la base de datos
   * Petición: LOGIN
   */
  
  @Test
  public void login_usuarioNoEncontradoNoAdmin(){
     
    String expResult = Codes.CODIGO_ERR_USER;
    
    MensajeRespuesta res =gestor.loginMens(userIncorrecto, passIncorrecta, false);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN)){      
      fail();
    }else{
     assertEquals(expResult, codeResult); 
    }    
  }
  
  /**
   * Comprueba que devuelve correctamente un código 61 cuando un usuario no se encuentra en la base de datos
   * Petición: LOGIN_ADMIN
   */
  @Test
  public void login_usuarioNoEncontradoAdmin(){       
    String expResult = Codes.CODIGO_ERR_USER;    
    MensajeRespuesta res =gestor.loginMens(userIncorrecto, passIncorrecta, true);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN_ADMIN)){      
      fail();
    }else{
     assertEquals(expResult, codeResult); 
    }    
  }
  
  
  /**
   * Comprueba que devuelve correctamente un código 61 cuando un usuario correcto intenta acceder a una sesión de administrador
   * Petición: LOGIN_ADMIN
   */

  @Test
  public void login_usuarioEncontradoNoAdmin(){       
    String expResult = Codes.CODIGO_ERR_USER;    
    MensajeRespuesta res =gestor.loginMens(userCorrecto, passCorrecta, true);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN_ADMIN)){      
      fail();
    }else{
     assertEquals(expResult, codeResult); 
    }    
  }

  
  /**
   * Comprueba que devuelve correctamente un código 62 cuando un usuario correcto accede con una pass incorretca
   * Petición: LOGIN
   */
  
  @Test
  public void login_usuarioEncontradoNoPassNoAdmin(){      
    String expResult = Codes.CODIGO_ERR_PWD;    
    MensajeRespuesta res =gestor.loginMens(userCorrecto, passIncorrecta, false);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN)){      
      fail();
    }else{
     assertEquals(expResult, codeResult); 
    }    
  }
  
    
  /**
   * Comprueba que devuelve correctamente un código 62 cuando un usuario correcto accede con una pass incorretca
   * Petición: LOGIN_ADMIN
   */
  @Test
  public void login_usuarioEncontradoNoPassAdmin(){        
    String expResult = Codes.CODIGO_ERR_PWD;    
    MensajeRespuesta res =gestor.loginMens(userCorrectoAdmin, passIncorrecta, true);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN_ADMIN)){      
      fail();
    }else{
     assertEquals(expResult, codeResult); 
    }    
  }
  
 
  /**
   * Comprueba que devuelve correctamente un código 10 con token cuando un usuario correcto accede con una pass correcta
   * Petición: LOGIN
   */  
  @Test
  public void login_usuarioEncontradoPassOkNoAdmin(){        
    String expResult = Codes.CODIGO_OK;    
    MensajeRespuesta res =gestor.loginMens(userCorrecto, passCorrecta, false);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN)){      
      fail();
    }else{
      if (res.getData()!=null){
        assertEquals(expResult, codeResult); 
      }else{
        fail();
      }     
    }    
  }
  

  
  /**
   * Comprueba que devuelve correctamente un código 10 con token cuando un usuario correcto accede con una pass correcta
   * Petición: LOGIN_ADMIN
   */ 
  @Test
  public void login_usuarioEncontradoPassOkAdmin(){        
    String expResult = Codes.CODIGO_OK;    
    MensajeRespuesta res =gestor.loginMens(userCorrectoAdmin, passCorrectaAdmin, true);
    String codeResult = res.getCode().getCode();
    String petResult = res.getPeticion();
    if (!petResult.equals(Mensaje.FUNCION_LOGIN_ADMIN)){      
      fail();
    }else{
      if (res.getData()!=null){
        assertEquals(expResult, codeResult); 
      }else{
        fail();
      }     
    }    
  }
  
  
  /*  
  *************
   CONSTRUCTOR SENTENCIA LOGIN
  ***************
  */
  
  /**
   * Comprueba que se construye la sentencia correctamente en caso de petición LOGIN
   */
  @Test
  public void constructorSentenciaLoginNoAdmin(){
    String email = "miMail";
    boolean isGestor = false;
    String sentencia = gestor.constructorSentenciaLogin(email, isGestor);
    String expResult = "select * from usuario where email='"+email+"'".toLowerCase();
    assertEquals (expResult, sentencia);            
  }
  /**
   * Comprueba que se construye la sentencia correctamente en caso de petición LOGIN_ADMIN
   */
  @Test
  public void constructorSentenciaLoginAdmin(){
    String email = "miMail";
    boolean isGestor = true;
    String sentencia = gestor.constructorSentenciaLogin(email, isGestor);
    String expResult = "select * from usuario where email='"+email+"' and isGestor='true'".toLowerCase();
    assertEquals (expResult, sentencia);            
  }
}
