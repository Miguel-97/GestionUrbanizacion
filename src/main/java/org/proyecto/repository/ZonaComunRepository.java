package org.proyecto.repository;

import org.proyecto.domain.ZonaComun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaComunRepository extends JpaRepository<ZonaComun, Long>{

}
