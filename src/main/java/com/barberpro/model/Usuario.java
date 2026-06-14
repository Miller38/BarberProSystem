package com.barberpro.model;

/**
*

* @author Miller
  */
  public class Usuario {

  private int id;

  private String nombre;
  private String usuario;
  private String password;
  private String rol;

  private String email;
  private String telefono;
  private String foto;

  private int estado;
  private int intentos;
  private int bloqueado;

  private String bloqueadoHasta;
  private String ultimoAcceso;
  private String fechaCreacion;

  public Usuario() {
  }

  public Usuario(String nombre, String usuario, String password, String rol) {  
   this.nombre = nombre;
   this.usuario = usuario;
   this.password = password;
   this.rol = rol;
 

  }

  public int getId() {
  return id;
  }

  public void setId(int id) {
  this.id = id;
  }

  public String getNombre() {
  return nombre;
  }

  public void setNombre(String nombre) {
  this.nombre = nombre;
  }

  public String getUsuario() {
  return usuario;
  }

  public void setUsuario(String usuario) {
  this.usuario = usuario;
  }

  public String getPassword() {
  return password;
  }

  public void setPassword(String password) {
  this.password = password;
  }

  public String getRol() {
  return rol;
  }

  public void setRol(String rol) {
  this.rol = rol;
  }

  public String getEmail() {
  return email;
  }

  public void setEmail(String email) {
  this.email = email;
  }

  public String getTelefono() {
  return telefono;
  }

  public void setTelefono(String telefono) {
  this.telefono = telefono;
  }

  public String getFoto() {
  return foto;
  }

  public void setFoto(String foto) {
  this.foto = foto;
  }

  public int getEstado() {
  return estado;
  }

  public void setEstado(int estado) {
  this.estado = estado;
  }

  public int getIntentos() {
  return intentos;
  }

  public void setIntentos(int intentos) {
  this.intentos = intentos;
  }

  public int getBloqueado() {
  return bloqueado;
  }

  public void setBloqueado(int bloqueado) {
  this.bloqueado = bloqueado;
  }

  public String getBloqueadoHasta() {
  return bloqueadoHasta;
  }

  public void setBloqueadoHasta(String bloqueadoHasta) {
  this.bloqueadoHasta = bloqueadoHasta;
  }

  public String getUltimoAcceso() {
  return ultimoAcceso;
  }

  public void setUltimoAcceso(String ultimoAcceso) {
  this.ultimoAcceso = ultimoAcceso;
  }

  public String getFechaCreacion() {
  return fechaCreacion;
  }

  public void setFechaCreacion(String fechaCreacion) {
  this.fechaCreacion = fechaCreacion;
  }

  @Override
  public String toString() {
  return nombre;
  }
  }
