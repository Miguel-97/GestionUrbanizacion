package org.proyecto.controller;

import org.proyecto.repository.EdificioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/vecino")
public class VecinoController {
	@Autowired
	private EdificioRepository repoEdificio;
}
