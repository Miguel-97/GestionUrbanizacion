package org.proyecto.domain;

import java.util.Date;
import javax.persistence.CascadeType;
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

	private Date fecha;

	private String hora;

	private String estado;

	@ManyToOne(cascade = CascadeType.ALL)
	private ZonaComun zona;

	// =========================================
	public Franja() {
		super();
		this.estado = "libre";
	}

	public Franja(Date fecha) {
		super();
		this.fecha = fecha;
		this.estado = "libre";
	}

	public Franja(Date fecha, String hora) {
		super();
		this.fecha = fecha;
		this.hora = hora;
		this.estado = "libre";
	}

	// =========================================

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date date) {
		this.fecha = date;
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
