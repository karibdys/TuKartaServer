/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ioc.tukartaserver.model;

/**
 *
 * @author Manu
 */
public class Login {
private String pwd;
private String email;
private boolean isGestor=true;

public Login(String pass, String mail, boolean gestor){
  this.pwd=pass;
  this.email=mail;
  this.isGestor=gestor;
}
  
}
