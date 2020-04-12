package org.proyecto.controller;

import java.util.List;

import org.proyecto.domain.Urbanizacion;
import org.proyecto.domain.ZonaComun;
import org.proyecto.exception.DangerException;
import org.proyecto.exception.InfoException;
import org.proyecto.helper.PRG;
import org.proyecto.repository.UrbanizacionRepository;
import org.proyecto.repository.ZonaComunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/zonaComun")
public class ZonaComunController {
	
	@Autowired
	private ZonaComunRepository repoZonaComun;
	
	@Autowired
	private UrbanizacionRepository repoUrbanizacion;
	
	//=========================================

	@GetMapping("c")
	public String c(ModelMap m) {
		m.put("urbanizaciones", repoUrbanizacion.findAll());
		m.put("view", "/zonaComun/c");
		return "/_t/frame";
	}
	
	@PostMapping("c")
	public String cPost(
			@RequestParam("nombre") String nombreZona,
			@RequestParam("horario") String horario,
			@RequestParam("tiempoMax") Integer tiempoMax,
			@RequestParam("aforoMax") Integer aforoMax,
			@RequestParam("urbaId") Long urbaId) throws DangerException, InfoException {
		
		try {
			ZonaComun zona = new ZonaComun(nombreZona, horario, tiempoMax, aforoMax);
			
			if(urbaId != null) {
				Urbanizacion urbanizacion = repoUrbanizacion.getOne(urbaId);
				
				urbanizacion.getZonasComunes().add(zona);
				zona.setCorresponde(urbanizacion);
			}
			repoZonaComun.save(zona);
			
		} catch (Exception e) {
			PRG.error("Zona común " + nombreZona + " duplicada", "/zonaComun/c");
		}
		PRG.info("Zona común " + nombreZona + " creada correctamente", "/zonaComun/r");
		
		return "redirect:/zonaComun/r";
	}
	
	//=========================================

	@GetMapping("r")
	public String r(ModelMap m) {
		List<ZonaComun> zonasComunes = repoZonaComun.findAll();
		m.put("zonasComunes", zonasComunes);
		m.put("view", "/zonaComun/r");
		return "/_t/frame";
	}
	
	
}
