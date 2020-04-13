package org.proyecto.controller;

import org.proyecto.repository.UrbanizacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/edificio")
public class EdificioController {
	@Autowired
	private UrbanizacionRepository repoUrbanizacion;
}
