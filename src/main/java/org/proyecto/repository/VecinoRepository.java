package org.proyecto.repository;

import org.proyecto.domain.Vecino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VecinoRepository extends JpaRepository<Vecino, String> {
}
