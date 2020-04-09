package org.proyecto.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Edificio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String portal;

	@OneToMany(mappedBy = "vive")
	private Collection<Vecino> vecinos;

	// ======================
	
	public Edificio() {
		super();
		this.vecinos = new ArrayList<Vecino>();
	}

	public Edificio(String portal) {
		super();

		this.portal = portal;
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

	public Collection<Vecino> getVecinos() {
		return vecinos;
	}

	public void setVecinos(Collection<Vecino> vecinos) {
		this.vecinos = vecinos;
	}

	// ======================

}
