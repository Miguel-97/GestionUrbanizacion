package org.proyecto.domain;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Franja {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Calendar fecha;

	private String hora;

	private String estado;

	@ManyToOne
	private ZonaComun zona;

	// =========================================
	public Franja() {
		super();
		this.estado = "libre";
	}

	public Franja(Calendar fecha) {
		super();
		this.fecha = fecha;
		this.estado = "libre";
	}

	public Franja(Calendar fecha, String hora) {
		super();
		this.fecha = fecha;
		this.hora = hora;
		this.estado = "libre";
	}

	// =========================================

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ZonaComun getZona() {
		return zona;
	}

	public void setZona(ZonaComun zona) {
		this.zona = zona;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
