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

	private boolean bajo;

	private Integer pisos;

	private Integer puertasXpiso;

	@ManyToOne
	private Urbanizacion pertenece;

	@OneToMany(mappedBy = "vive", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Collection<Vecino> vecinos;

	// ======================

	public Edificio() {
		super();
		this.vecinos = new ArrayList<Vecino>();
	}
	
	public Edificio(String portal) {
		super();
		this.portal = portal;
	}

	public Edificio(String portal, boolean bajo, Integer pisos, Integer puertasXpiso) {
		super();
		this.portal = portal;
		this.bajo = bajo;
		this.pisos = pisos;
		this.puertasXpiso = puertasXpiso;
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

	
	public boolean getBajo() {
		return bajo;
	}

	public void setBajo(boolean bajo) {
		this.bajo = bajo;
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