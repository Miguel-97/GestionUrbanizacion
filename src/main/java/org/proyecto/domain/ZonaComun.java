package org.proyecto.domain;

import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class ZonaComun {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String nombre;

	private String horario;

	private Integer tiempoMax;

	private Integer aforoMax;

	@ManyToOne
	private Urbanizacion corresponde;

	@OneToMany(mappedBy = "tiene", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Collection<Reserva> reservas;

	@OneToMany(mappedBy = "zona", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private Collection<Franja> franjas;
	// =========================================

	public ZonaComun(String nombre) {
		this.nombre = nombre;
		this.franjas = new ArrayList<Franja>();
		this.reservas = new ArrayList<Reserva>();
	}

	public ZonaComun(String nombre, String horario, Integer tiempoMax, Integer aforoMax) {
		this.nombre = nombre;
		this.horario = horario;
		this.tiempoMax = tiempoMax;
		this.aforoMax = aforoMax;
		this.franjas = new ArrayList<Franja>();
		this.reservas = new ArrayList<Reserva>();
	}

	public ZonaComun() {
		this.reservas = new ArrayList<Reserva>();
		this.franjas = new ArrayList<Franja>();
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

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public Integer getTiempoMax() {
		return tiempoMax;
	}

	public void setTiempoMax(Integer tiempoMax) {
		this.tiempoMax = tiempoMax;
	}

	public Integer getAforoMax() {
		return aforoMax;
	}

	public void setAforoMax(Integer aforoMax) {
		this.aforoMax = aforoMax;
	}

	public Urbanizacion getCorresponde() {
		return corresponde;
	}

	public void setCorresponde(Urbanizacion corresponde) {
		this.corresponde = corresponde;
	}

	public Collection<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(Collection<Reserva> reservas) {
		this.reservas = reservas;
	}

	public Collection<Franja> getFranjas() {
		return franjas;
	}

	public void setFranjas(Collection<Franja> franjas) {
		this.franjas = franjas;
	}

}
