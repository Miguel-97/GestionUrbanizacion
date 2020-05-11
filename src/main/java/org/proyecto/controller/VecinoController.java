package org.proyecto.controller;

import java.util.List;
import org.proyecto.domain.Vecino;
import org.proyecto.repository.VecinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/vecino")
public class VecinoController {

	@Autowired
	private VecinoRepository repoVecino;
	
	// =========================================

	@GetMapping("r")
	public String r(ModelMap m) {
		List<Vecino> vecinos = repoVecino.findAll();
		m.put("vecinos", vecinos);
		m.put("view", "/vecino/r");
		return "/_t/frame";
	}
	
}