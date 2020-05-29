package org.proyecto.repository;

import org.proyecto.domain.Urbanizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrbanizacionRepository extends JpaRepository<Urbanizacion, Long>{

	public Urbanizacion getByNombre(String nombre);

}
