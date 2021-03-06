package org.proyecto.repository;

import java.util.List;
import org.proyecto.domain.Edificio;
import org.proyecto.domain.Vecino;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VecinoRepository extends JpaRepository<Vecino, String> {

	public Vecino getByUsername(String username);

	public Vecino getByEmail(String email);

	public List<Vecino> findByEstado(String estado);

	public List<Vecino> findByViveId(Long Edificioid);

	public List<Vecino> findByVivePortal(String portal);

	public List<Vecino> findByVive(Edificio edificio);

	public List<Vecino> findByViveIdOrderByUsernameAsc(Long Edificioid);

	public List<Vecino> findByViveIdOrderByUsernameDesc(Long Edificioid);
}
