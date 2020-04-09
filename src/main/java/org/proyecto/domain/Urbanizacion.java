package org.proyecto.domain;

import java.util.ArrayList;
import java.util.Collection;
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
	
	@OneToMany(mappedBy = "pertenece")
	private Collection<Edificio> edificios;
	
	//=========================================
	
	public Urbanizacion(String nombre) {
		this.nombre = nombre;
		this.edificios = new ArrayList<Edificio>();
	}
	
	public Urbanizacion() {
		this.edificios = new ArrayList<>();
	}

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

	public Collection<Edificio> getEdificios() {
		return edificios;
	}

	public void setEdificios(Collection<Edificio> edificios) {
		this.edificios = edificios;
	}
	
	
	
}
