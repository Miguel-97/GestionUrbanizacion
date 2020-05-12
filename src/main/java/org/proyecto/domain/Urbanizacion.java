package org.proyecto.domain;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Urbanizacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String nombre;
	
	private String estado;

	@OneToMany(mappedBy = "pertenece", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Collection<Edificio> edificios;

	@OneToMany(mappedBy = "corresponde", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Collection<ZonaComun> zonasComunes;

	// =========================================

	public Urbanizacion(String nombre, String estado) {
		this.nombre = nombre;
		this.estado = estado;
		this.edificios = new ArrayList<Edificio>();
		this.zonasComunes = new ArrayList<ZonaComun>();
	}

	public Urbanizacion() {
		this.edificios = new ArrayList<>();
		this.zonasComunes = new ArrayList<ZonaComun>();
	}

	// =========================================

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Collection<Edificio> getEdificios() {
		return edificios;
	}

	public void setEdificios(Collection<Edificio> edificios) {
		this.edificios = edificios;
	}

	public Collection<ZonaComun> getZonasComunes() {
		return zonasComunes;
	}

	public void setZonasComunes(Collection<ZonaComun> zonasComunes) {
		this.zonasComunes = zonasComunes;
	}

}
