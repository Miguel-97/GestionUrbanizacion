package org.proyecto.controller;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import org.proyecto.domain.Reserva;
import org.proyecto.domain.Vecino;
import org.proyecto.domain.ZonaComun;
import org.proyecto.exception.DangerException;
import org.proyecto.exception.InfoException;
import org.proyecto.helper.PRG;
import org.proyecto.helper.helper;
import org.proyecto.repository.ReservaRepository;
import org.proyecto.repository.VecinoRepository;
import org.proyecto.repository.ZonaComunRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/reserva")
public class ReservaController {

	@Autowired
	private ZonaComunRepository repoZonaComun;

	@Autowired
	private ReservaRepository repoReserva;

	@Autowired
	private VecinoRepository repoVecino;

	// =========================================

	@GetMapping("c")
	public String c(ModelMap m) {
		m.put("zonas", repoZonaComun.findAll());
		m.put("vecinos", repoVecino.findAll());
		m.put("view", "/reserva/c");
		return "/_t/frame";
	}

	@PostMapping("c")
	public String cPost(@RequestParam("fecha") Calendar fecha, @RequestParam("inicio") String inicio,
			@RequestParam("nBloques") Integer nBloques, @RequestParam("vecinoId") String vecinoId,
			@RequestParam("zonaId") Long zonaId) throws DangerException, InfoException {

		try {
			Reserva reserva = new Reserva(fecha, inicio, nBloques);

			if (vecinoId != null && zonaId != null) {
				Vecino vecino = repoVecino.getOne(vecinoId);
				ZonaComun zona = repoZonaComun.getOne(zonaId);

				vecino.getReservas().add(reserva);
				zona.getReservas().add(reserva);

				reserva.setHace(vecino);
				reserva.setTiene(zona);
			}
			repoReserva.save(reserva);

		} catch (Exception e) {
			PRG.error("Reserva no realizada ", "/reserva/c");
		}
		PRG.info("Reserva realizada correctamente", "/reserva/r");

		return "redirect:/reserva/r";
	}

	// =========================================

	@GetMapping("r")
	public String r(ModelMap m) {
		List<Reserva> reservas = repoReserva.findAll();
		m.put("reservas", reservas);
		m.put("view", "/reserva/r");
		return "/_t/frame";
	}

	@PostMapping("d")
	public String d(@RequestParam("idR") Long idR) throws DangerException {
		try {
			Reserva reserva = repoReserva.getOne(idR);
			// ==========historico========== Guarda la reserva seleccionada
			helper.historicoReserva(reserva);
			System.out.println(helper.leerArchivo("reservas"));
			// ==========historico==========
			repoReserva.delete(reserva);
		} catch (Exception e) {
			PRG.error("Error al borrar reserva", "/reserva/r");
		}
		return "redirect:/reserva/r";
	}

	@Scheduled(cron = "0 50 23 * * *", zone = "Europe/Madrid")
	public void completarReservasPendientes() {
		LocalDate fechaHoy = LocalDate.now();
		List<Reserva> reservasPend = repoReserva.findByFechaAndEstado(fechaHoy, "pendiente");
		for (int i = 0; i < reservasPend.size(); i++) {
			reservasPend.get(i).setEstado("completada");
		}
		
	}
}
