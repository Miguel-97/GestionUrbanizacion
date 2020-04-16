package org.proyecto.controller;

import java.util.List;
import org.proyecto.domain.Urbanizacion;
import org.proyecto.exception.DangerException;
import org.proyecto.exception.InfoException;
import org.proyecto.helper.PRG;
import org.proyecto.repository.UrbanizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/urbanizacion")
public class UrbanizacionController {
	
	@Autowired
	private UrbanizacionRepository repoUrbanizacion;
	
	//=========================================

	@GetMapping("c")
	public String c(ModelMap m) {
		m.put("view", "/urbanizacion/c");
		return "/_t/frame";
	}
	
	@PostMapping("c")
	public String cPost(@RequestParam("nombre") String nombreUrba) throws DangerException, InfoException {
		
		try {
			repoUrbanizacion.save(new Urbanizacion(nombreUrba));
		}
		catch (Exception e) {
			PRG.error("Urbanización " + nombreUrba + " duplicada", "/urbanizacion/c");
		}
		PRG.info("Urbanización " + nombreUrba + " creada correctamente", "urbanizacion/r");
		
		return "redirect:/urbanizacion/r";
	}
	
	//=========================================

	@GetMapping("r")
	public String r(ModelMap m) {
		List<Urbanizacion> urbanizaciones = repoUrbanizacion.findAll();
		m.put("urbanizaciones", urbanizaciones);
		m.put("view", "/urbanizacion/r");
		return "/_t/frame";
	}

	//=========================================

	@PostMapping("d")
	public String dPost(@RequestParam("urbaId") Long urbaId) throws DangerException {
		String nombreUrba = "----";
		try {
			Urbanizacion urbanizacion = repoUrbanizacion.getOne(urbaId);
			nombreUrba = urbanizacion.getNombre();
			repoUrbanizacion.delete(urbanizacion);
		} catch (Exception e) {
			PRG.error("Error al borrar la urbanización " + nombreUrba, "/urbanizacion/r");
		}
		return "redirect:/urbanizacion/r";
	}
	
	
}
