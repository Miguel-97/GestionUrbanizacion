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
	public void crearPost(@RequestParam("portal") String portal, @RequestParam("pisos") Integer pisos,
			@RequestParam("puertasXpiso") Integer puertasXpiso, @RequestParam("denominacion") String denominacion,
			@RequestParam(value = "urbaId", required = false) Long urbaId, HttpSession s)
			throws DangerException, InfoException {
		try {
			Edificio edificio = new Edificio(portal, pisos, puertasXpiso, denominacion);

			Urbanizacion urbanizacion = repoUrbanizacion.getOne(urbaId);
			urbanizacion.getEdificios().add(edificio);
			edificio.setPertenece(urbanizacion);

			// TODO C vecinos

			ArrayList<Character> letras = new ArrayList<Character>();
			for (int i = 0; i < puertasXpiso; i++) {
				letras.add((char) (65 + i));
			}
			ArrayList<Vecino> vecindad = new ArrayList<Vecino>();
			for (int j = 0; j < pisos; j++) {
				for (int i = 0; i < puertasXpiso; i++) {
					if (denominacion == "l") {
						String id = urbanizacion.getNombre() + "_" + edificio.getPortal() + "_" + j + "_"
								+ letras.get(i);
						String username = urbanizacion.getNombre() + "_" + edificio.getPortal() + "_" + j + "_"
								+ letras.get(i);
						String password = "aleatoria";
						Vecino vecino = new Vecino(id, username, password);
						vecino.setVive(edificio);
						vecindad.add(vecino);
						repoVecino.save(vecino);
					} else if (denominacion == "n") {
						String id = urbanizacion.getNombre() + "_" + edificio.getPortal() + "_" + j + "_" + i;
						String username = urbanizacion.getNombre() + "_" + edificio.getPortal() + "_" + j + "_" + i;
						String password = "aleatoria";
						Vecino vecino = new Vecino(id, username, password);
						vecino.setVive(edificio);
						vecindad.add(vecino);
						repoVecino.save(vecino);
					} else {
						PRG.error("Error al crear algun vecino con la denominacion " + denominacion, "/edificio/c");
					}
				}
			}
			// Fin C Vecinos
			
			
			edificio.setVecinos(vecindad);
			repoEdificio.save(edificio);
		} catch (Exception e) {
			PRG.error("Edificio duplicado", "/edificio/c");
		}
		PRG.info("Edificio creado correctamente", "/edificio/r");
	}
	

	@GetMapping("r")
	public String r(ModelMap m) {
		List<Edificio> edificios = repoEdificio.findAll();
		m.put("edificios", edificios);
		m.put("view", "/edificio/r");
		return "/_t/frame";
	}

}
