package com.example.pruebagmaps;

public class usuario {
	private int idUsuario;
	private String Nombre;
	private String Descripcion;
	private int idImagen;
	private String Correo;
	private String Password;
	private int idtipousuario;
	
	public usuario(){}
	
	/**
	 * @return the idUsuario
	 */
	public int getIdUsuario() {
		return idUsuario;
	}
	/**
	 * @param idUsuario the idUsuario to set
	 */
	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return Nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		Nombre = nombre;
	}
	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return Descripcion;
	}
	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		Descripcion = descripcion;
	}
	/**
	 * @return the idImagen
	 */
	public int getIdImagen() {
		return idImagen;
	}
	/**
	 * @param idImagen the idImagen to set
	 */
	public void setIdImagen(int idImagen) {
		this.idImagen = idImagen;
	}
	/**
	 * @return the correo
	 */
	public String getCorreo() {
		return Correo;
	}
	/**
	 * @param correo the correo to set
	 */
	public void setCorreo(String correo) {
		Correo = correo;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return Password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		Password = password;
	}
	/**
	 * @return the idtipousuario
	 */
	public int getIdtipousuario() {
		return idtipousuario;
	}
	/**
	 * @param idtipousuario the idtipousuario to set
	 */
	public void setIdtipousuario(int idtipousuario) {
		this.idtipousuario = idtipousuario;
	}
}
