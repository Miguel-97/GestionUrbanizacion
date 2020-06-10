package org.proyecto.repository;

import java.util.Date;
import java.util.List;

import org.proyecto.domain.Reserva;
import org.proyecto.domain.Vecino;
import org.proyecto.domain.ZonaComun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

	public List<Reserva> findByFechaAndEstado(Date fechaHoy, String estado);
	public List<Reserva> findByHace(Vecino vecino);
	public List<Reserva> findByTiene(ZonaComun zona);

}
