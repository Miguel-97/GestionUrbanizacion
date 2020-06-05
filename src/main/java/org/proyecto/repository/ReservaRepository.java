package org.proyecto.repository;

import java.time.LocalDate;
import java.util.List;

import org.proyecto.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

	public List<Reserva> findByFechaAndEstado(LocalDate fechaHoy, String estado);

}
