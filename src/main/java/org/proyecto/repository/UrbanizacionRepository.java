package org.proyecto.repository;

import org.proyecto.domain.Edificio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrbanizacionRepository extends JpaRepository<Edificio, Long>{

}
