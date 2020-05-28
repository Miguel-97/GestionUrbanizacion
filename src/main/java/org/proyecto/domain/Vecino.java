package org.proyecto.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class Vecino {

	@Id
	@Column(unique = true)
	private String id;

	private String nombre;

	private String username;

	private String password;

	@Column(unique=true)
	private String email;

	private String estado;

	@ManyToOne(cascade = CascadeType.ALL)
	private Edificio vive;

	@OneToMany(mappedBy = "hace")
	private Collection<Reserva> reservas;

	// ======================

	public Vecino() {
		super();
		this.reservas = new ArrayList<Reserva>();
	}

	public Vecino(String id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.estado = "inactivo";
		this.reservas = new ArrayList<Reserva>();
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
		this.password = (new BCryptPasswordEncoder()).encode(password);
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

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Collection<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(Collection<Reserva> reservas) {
		this.reservas = reservas;
	}
}
