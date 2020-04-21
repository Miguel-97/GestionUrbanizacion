package org.proyecto.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.proyecto.domain.Edificio;
import org.proyecto.domain.Urbanizacion;
import org.proyecto.domain.Vecino;
import org.proyecto.exception.DangerException;
import org.proyecto.exception.InfoException;
import org.proyecto.helper.PRG;
import org.proyecto.repository.EdificioRepository;
import org.proyecto.repository.UrbanizacionRepository;
import org.proyecto.repository.VecinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/edificio")
public class EdificioController {
	@Autowired
	private UrbanizacionRepository repoUrbanizacion;
	@Autowired
	private EdificioRepository repoEdificio;
	@Autowired
	private VecinoRepository repoVecino;

	@GetMapping("c")
	public String crearGet(ModelMap m, HttpSession s) throws DangerException {
		m.put("urbanizaciones", repoUrbanizacion.findAll());
		m.put("view", "/edificio/c");
		return "/_t/frame";
	}

	@PostMapping("c")
	public String crearPost(@RequestParam("portal") String portal, @RequestParam("pisos") Integer pisos,
			@RequestParam("puertasXpiso") Integer puertasXpiso, @RequestParam("denominacion") String denominacion,
			@RequestParam("urbaId") Long urbaId) throws DangerException, InfoException {
		try {
			Edificio edificio = new Edificio(portal, pisos, puertasXpiso, denominacion);
			if (urbaId != null) {
				Urbanizacion urbanizacion = repoUrbanizacion.getOne(urbaId);

				urbanizacion.getEdificios().add(edificio);
				edificio.setPertenece(urbanizacion);

				// TODO C vecinos Genera tantos edificios como vecinos hay

				ArrayList<Character> letras = new ArrayList<Character>();
				for (int i = 0; i < puertasXpiso; i++) {
					letras.add((char) (65 + i));
				}

				for (int j = 0; j < pisos; j++) {
					for (int i = 0; i < puertasXpiso; i++) {
						//No se por que no me entra en el if, salta al else, y en teoria le llega la denominacion, pero aun asi, no entra en el if
						if (denominacion == "numeros") {
							String id = urbanizacion.getNombre() + "_" + portal + "_" + (j + 1) + "_" + i;
							String username = "Vecino_" + urbanizacion.getNombre() + "_" + portal + "_" + (j + 1) + "_"
									+ i;
							String password = "aleatoria";
							Vecino vecino = new Vecino(id, username, password);

							vecino.setVive(edificio);
							edificio.getVecinos().add(vecino);
							repoVecino.save(vecino);

						} else {
							String id = urbanizacion.getNombre() + "_" + portal + "_" + (j + 1) + "_" + letras.get(i);
							String username = "Vecino_" + urbanizacion.getNombre() + "_" + portal + "_" + (j + 1) + "_"
									+ letras.get(i);
							String password = "aleatoria";
							Vecino vecino = new Vecino(id, username, password);

							vecino.setVive(edificio);
							edificio.getVecinos().add(vecino);
							repoVecino.save(vecino);
						}
					}
				}
				// Fin C Vecinos

			}

			repoEdificio.save(edificio);

		} catch (Exception e) {
			PRG.error("Edificio duplicado", "/edificio/c");
		}

		PRG.info("Edificio creado correctamente", "/edificio/r");

		return "redirect:/edificio/r";
	}

	@GetMapping("r")
	public String r(ModelMap m) {
		List<Edificio> edificios = repoEdificio.findAll();
		m.put("edificios", edificios);
		m.put("view", "/edificio/r");
		return "/_t/frame";
	}

}
