package org.proyecto.domain;

import java.sql.Date;

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

	private Integer nBloques;

	@ManyToOne
	private ZonaComun tiene;

	@ManyToOne
	private Vecino hace;

	// =========================================

	public Reserva() {
		super();

	}

	public Reserva(Date fecha, String inicio, Integer nBloques) {
		super();
		this.fecha = fecha;
		this.inicio = inicio;
		this.nBloques = nBloques;
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

	public Integer getnBloques() {
		return nBloques;
	}

	public void setnBloques(Integer nBloques) {
		this.nBloques = nBloques;
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

}