package org.proyecto.repository;

import java.util.Date;
import java.util.List;

import org.proyecto.domain.Franja;
import org.proyecto.domain.ZonaComun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranjaRepository extends JpaRepository<Franja, Long> {

	public List<Franja> findByFecha(Date fecha);

	public List<Franja> findByEstado(String estado);
	
	public Franja getByFechaAndHora(Date fecha,String hora);

	public List<Franja> findByFechaAndEstado(Date fecha, String estado);

	public List<Franja> findByZonaAndFechaAndEstado(ZonaComun zona, Date fecha, String estado);

}
