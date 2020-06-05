package org.proyecto.repository;

import java.util.List;

import org.proyecto.domain.Urbanizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrbanizacionRepository extends JpaRepository<Urbanizacion, Long> {

	public List<Urbanizacion> findByNombre(String nombre);

	public List<Urbanizacion> findByEstado(String estado);

	public Urbanizacion getByNombre(String nombre);


}
