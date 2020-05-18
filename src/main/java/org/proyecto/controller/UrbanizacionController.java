package org.proyecto.controller;

import java.util.ArrayList;
import java.util.List;

import org.proyecto.domain.Edificio;
import org.proyecto.domain.Reserva;
import org.proyecto.domain.Urbanizacion;
import org.proyecto.domain.Vecino;
import org.proyecto.domain.ZonaComun;
import org.proyecto.exception.DangerException;
import org.proyecto.helper.PRG;
import org.proyecto.helper.helper;
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

	// =========================================

	@GetMapping("c")
	public String c(ModelMap m) {
		m.put("view", "/urbanizacion/c");
		return "/_t/frame";
	}

	@PostMapping("c")
	public String cPost(@RequestParam("nombre") String nombreUrba) throws DangerException {
		if(nombreUrba.equals("")) {
			PRG.error("Datos vacios, rellene todos los datos", "/urbanizacion/c");
		}
		else {
			try {
				repoUrbanizacion.save(new Urbanizacion(nombreUrba));
			} catch (Exception e) {
				PRG.error("Urbanización " + nombreUrba + " duplicada", "/urbanizacion/c");
			}
		}

		return "redirect:/urbanizacion/r";
	}

	// =========================================

	@GetMapping("r")
	public String r(ModelMap m) {
		List<Urbanizacion> urbanizaciones = repoUrbanizacion.findAll();
		m.put("urbanizaciones", urbanizaciones);
		m.put("view", "/urbanizacion/r");
		return "/_t/frame";
	}

	// =========================================

	@GetMapping("u")
	public String u(ModelMap m, @RequestParam("urbaId") Long urbaId) {
		m.put("urbanizacion", repoUrbanizacion.getOne(urbaId));
		m.put("view", "/urbanizacion/u");
		return "/_t/frame";
	}

	@PostMapping("u")
	public String uPost(@RequestParam("urbaId") Long urbaId, @RequestParam("nombre") String nombreUrba)
			throws DangerException {

		try {
			Urbanizacion urba = repoUrbanizacion.getOne(urbaId);
			urba.setNombre(nombreUrba);
			repoUrbanizacion.save(urba);
		} catch (Exception e) {
			PRG.error("La urbanización no pudo ser actualizada.", "/urbanizacion/r");
		}
		return "redirect:/urbanizacion/r";
	}

	// =========================================

	@PostMapping("d")
	public String dPost(@RequestParam("urbaId") Long urbaId) throws DangerException {
		String nombreUrba = "----";
		try {
			Urbanizacion urbanizacion = repoUrbanizacion.getOne(urbaId);

			// ==========historico==========
			// Guarda la urbanizacion, vecinos, edificios, zonas y las reservas de esta
			// urbanizacion
			ArrayList<Edificio> edificios = new ArrayList<>();
			ArrayList<Vecino> vecinos = new ArrayList<>();
			ArrayList<ZonaComun> zonas = new ArrayList<>();
			ArrayList<Reserva> reservas = new ArrayList<>();
			edificios.addAll(urbanizacion.getEdificios());
			for (Edificio edificio : edificios) {
				vecinos.addAll(edificio.getVecinos());
			}
			zonas.addAll(urbanizacion.getZonasComunes());
			for (Vecino vecino : vecinos) {
				reservas.addAll(vecino.getReservas());
			}
			helper.historicoUrbanizacion(urbanizacion, edificios, vecinos, zonas, reservas);
			System.out.println(helper.leerArchivo("urbanizaciones"));
			System.out.println(helper.leerArchivo("edificios"));
			System.out.println(helper.leerArchivo("vecinos"));
			System.out.println(helper.leerArchivo("zonas"));
			System.out.println(helper.leerArchivo("reservas"));
			// ==========historico==========
			nombreUrba = urbanizacion.getNombre();
			repoUrbanizacion.delete(urbanizacion);
		} catch (Exception e) {
			PRG.error("Error al borrar la urbanización " + nombreUrba, "/urbanizacion/r");
		}
		return "redirect:/urbanizacion/r";
	}

}
