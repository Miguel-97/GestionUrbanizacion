package org.proyecto.repository;

import java.util.List;

import org.proyecto.domain.Edificio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EdificioRepository extends JpaRepository<Edificio, Long> {
	public Edificio getByPortal(String portal);
	public List<Edificio> findByPortal(Long urbaId);
	public List<Edificio> findByPerteneceId(Long urbaId);
	public List<Edificio> findByPerteneceIdAndPortal(Long urbaId, String portal);
	//public List<Edificio> findByPerteneceIdAndPortalAndPiso(Long idUrba, String portal, String piso);
	public Edificio getByPerteneceIdAndPortal(Long urbaId, String portal);
	
}
