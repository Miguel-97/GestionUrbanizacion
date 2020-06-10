package org.proyecto.repository;

import java.util.List;
import org.proyecto.domain.Edificio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdificioRepository extends JpaRepository<Edificio, Long> {

	public Edificio getByPerteneceIdAndPortal(Long urbaId, String portal);

	public List<Edificio> findByPerteneceId(Long urbaId);

	public List<Edificio> findByPerteneceIdOrderByPortalAsc(Long urbaId);

	public List<Edificio> findByPerteneceIdOrderByPortalDesc(Long urbaId);

}
