package org.proyecto.controller;

import java.util.ArrayList;
import java.util.List;

import org.proyecto.domain.Reserva;
import org.proyecto.domain.Urbanizacion;
import org.proyecto.domain.ZonaComun;
import org.proyecto.exception.DangerException;
import org.proyecto.helper.PRG;
import org.proyecto.helper.helper;
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

	// =========================================

	@GetMapping("c")
	public String c(ModelMap m) {
		m.put("urbanizaciones", repoUrbanizacion.findAll());
		m.put("view", "/zonaComun/c");
		return "/_t/frame";
	}

	@PostMapping("c")
	public String cPost(@RequestParam("nombre") String nombreZona, @RequestParam("horario") String horario,
			@RequestParam("tiempoMax") Integer tiempoMax, @RequestParam("aforoMax") Integer aforoMax,
			@RequestParam("urbaId") Long urbaId) throws DangerException {

		if(nombreZona == null || nombreZona.trim().equals("") || horario == null || horario.trim().equals("") || tiempoMax == null || aforoMax == null || urbaId == null) {
			PRG.error("Datos vacios y/o negativos, rellene los datos correctamente", "/zonaComun/c");

		}
		else {
			try {
				ZonaComun zona = new ZonaComun(helper.cadenaLetrasMayMin(nombreZona), horario, tiempoMax, aforoMax);

				if (urbaId != null) {
					Urbanizacion urbanizacion = repoUrbanizacion.getOne(urbaId);

					urbanizacion.getZonasComunes().add(zona);
					zona.setCorresponde(urbanizacion);
				}
				repoZonaComun.save(zona);

			} catch (Exception e) {
				PRG.error("Zona común " + nombreZona + " duplicada", "/zonaComun/c");
			}
			
		}

		return "redirect:/zonaComun/r";
	}

	// =========================================

	@GetMapping("r")
	public String r(ModelMap m) {
		List<ZonaComun> zonasComunes = repoZonaComun.findAll();
		m.put("zonasComunes", zonasComunes);
		m.put("view", "/zonaComun/r");
		return "/_t/frame";
	}

	// =========================================

	@GetMapping("u")
	public String u(ModelMap m, @RequestParam("zonaComunId") Long zonaComunId) {
		m.put("zonaComun", repoZonaComun.getOne(zonaComunId));
		m.put("urbanizaciones", repoUrbanizacion.findAll());
		m.put("view", "/zonaComun/u");
		return "/_t/frame";
	}

	@PostMapping("u")
	public String uPost(@RequestParam("zonaComunId") Long zonaComunId, @RequestParam("nombre") String nombreZona,
			@RequestParam("horario") String horario, @RequestParam("tiempoMax") Integer tiempoMax,
			@RequestParam("aforoMax") Integer aforoMax, @RequestParam("urbaId") Long urbaId) throws DangerException {

		try {
			ZonaComun zona = repoZonaComun.getOne(zonaComunId);

			// ATRIBUTOS NORMALES
			zona.setNombre(nombreZona);
			zona.setHorario(horario);
			zona.setTiempoMax(tiempoMax);
			zona.setAforoMax(aforoMax);

			// URBANIZACION CORRESPONDE
			Urbanizacion urbaCorresponde = repoUrbanizacion.getOne(urbaId);
			Urbanizacion urbaCorrespondeAnt = zona.getCorresponde();

			urbaCorrespondeAnt.getZonasComunes().remove(zona);
			zona.setCorresponde(null);

			urbaCorresponde.getZonasComunes().add(zona);
			zona.setCorresponde(urbaCorresponde);

			repoZonaComun.save(zona);
		} catch (Exception e) {
			PRG.error("La zona común no pudo ser actualizada.", "/zonaComun/r");
		}
		return "redirect:/zonaComun/r";
	}

	// =========================================

	@PostMapping("d")
	public String dPost(@RequestParam("zonaComunId") Long zonaComunId) throws DangerException {
		String nombreZona = "----";
		try {
			ZonaComun zona = repoZonaComun.getOne(zonaComunId);
			// ==========historico==========
			// Guarda la zona y las reservas realizadas en esta zona
			ArrayList<Reserva> reservas = new ArrayList<>();
			reservas.addAll(zona.getReservas());
			helper.historicoZonaComun(zona, reservas);
			System.out.println(helper.leerArchivo("zonas"));
			System.out.println(helper.leerArchivo("reservas"));
			// ==========historico==========
			nombreZona = zona.getNombre();
			repoZonaComun.delete(zona);
		} catch (Exception e) {
			PRG.error("Error al borrar la zona común " + nombreZona, "/zonaComun/r");
		}
		return "redirect:/zonaComun/r";
	}

}
