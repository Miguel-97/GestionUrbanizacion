package org.proyecto.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Edificio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String portal;

	private Integer pisos;

	private Integer puertasXpiso;

	private String denominacion;

	@ManyToOne
	private Urbanizacion pertenece;

	@OneToMany(mappedBy = "vive", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Collection<Vecino> vecinos;

	// ======================

	public Edificio() {
		super();
		this.vecinos = new ArrayList<Vecino>();
	}

	public Edificio(String portal, Integer pisos, Integer puertasXpiso, String denominacion) {
		super();
		this.portal = portal;
		this.pisos = pisos;
		this.puertasXpiso = puertasXpiso;
		this.denominacion = denominacion;
		this.vecinos = new ArrayList<Vecino>();

	}
	// ======================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPortal() {
		return portal;
	}

	public void setPortal(String portal) {
		this.portal = portal;
	}

	public Integer getPisos() {
		return pisos;
	}

	public void setPisos(Integer pisos) {
		this.pisos = pisos;
	}

	public Integer getPuertasXpiso() {
		return puertasXpiso;
	}

	public void setPuertasXpiso(Integer puertasXpiso) {
		this.puertasXpiso = puertasXpiso;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public Urbanizacion getPertenece() {
		return pertenece;
	}

	public void setPertenece(Urbanizacion pertenece) {
		this.pertenece = pertenece;
	}

	public Collection<Vecino> getVecinos() {
		return vecinos;
	}

	public void setVecinos(Collection<Vecino> vecinos) {

		this.vecinos = vecinos;
	}

	// ======================

}
