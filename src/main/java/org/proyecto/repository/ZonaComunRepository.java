package org.proyecto.repository;

import java.util.List;
import org.proyecto.domain.Urbanizacion;
import org.proyecto.domain.ZonaComun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaComunRepository extends JpaRepository<ZonaComun, Long> {
	
	public List<ZonaComun> findByNombre(String nombre);
	public List<ZonaComun> findByCorresponde(Urbanizacion urba);
}
