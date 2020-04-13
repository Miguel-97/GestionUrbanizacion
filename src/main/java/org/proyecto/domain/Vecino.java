package org.proyecto.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Vecino {

	@Id
	@Column(unique = true)
	private String id;

	private String nombre;

	@Column(unique = true)
	private String username;

	private String password;

	@Column(unique = true)
	private String email;

	@ManyToOne
	private Edificio vive;

	// ======================

	public Vecino() {
		super();
	}

	public Vecino(String id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		
	}

	public Vecino(String nombre, String email, Edificio vive) {
		super();
		this.nombre = nombre;
		this.email = email;
		this.vive = vive;
	}

	public Vecino(String nombre, String username, String password, String email, Edificio vive) {
		super();
		this.nombre = nombre;
		this.username = username;
		this.password = password;
		this.email = email;
		this.vive = vive;
	}

	// ======================

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Edificio getVive() {
		return vive;
	}

	public void setVive(Edificio vive) {
		this.vive = vive;
	}
}
