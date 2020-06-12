package org.proyecto.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Date fecha;

	private String inicio;

	private Integer tReserva;

	private String estado;

	@ManyToOne
	private ZonaComun tiene;

	@ManyToOne
	private Vecino hace;

	// =========================================

	public Reserva() {
		super();

	}

	public Reserva(Date fecha, String inicio, Integer tReserva) {
		super();
		this.fecha = fecha;
		this.inicio = inicio;
		this.tReserva = tReserva;
		this.estado = "pendiente";
	}

	// =========================================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getInicio() {
		return inicio;
	}

	public void setInicio(String inicio) {
		this.inicio = inicio;
	}

	public Integer gettReserva() {
		return tReserva;
	}

	public void settReserva(Integer tReserva) {
		this.tReserva = tReserva;
	}

	public ZonaComun getTiene() {
		return tiene;
	}

	public void setTiene(ZonaComun tiene) {
		this.tiene = tiene;
	}

	public Vecino getHace() {
		return hace;
	}

	public void setHace(Vecino hace) {
		this.hace = hace;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

}