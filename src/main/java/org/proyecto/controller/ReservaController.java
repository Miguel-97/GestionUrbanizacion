package org.proyecto.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.proyecto.domain.Franja;
import org.proyecto.domain.Reserva;
import org.proyecto.domain.Vecino;
import org.proyecto.domain.ZonaComun;
import org.proyecto.exception.DangerException;
import org.proyecto.exception.InfoException;
import org.proyecto.helper.PRG;
import org.proyecto.helper.helper;
import org.proyecto.helper.rol;
import org.proyecto.repository.FranjaRepository;
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
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/reserva")
public class ReservaController {

	@Autowired
	private ZonaComunRepository repoZonaComun;

	@Autowired
	private ReservaRepository repoReserva;

	@Autowired
	private VecinoRepository repoVecino;

	@Autowired
	private FranjaRepository repoFranja;

	// =========================================

	@GetMapping("c")
	public String c(@RequestParam("zonaId") Long zonaId, @RequestParam("vecinoId") String vecinoId, ModelMap m,
			HttpSession s) throws DangerException {
			rol.isRolOK("auth", s);
			m.put("zona", repoZonaComun.getOne(zonaId));
			m.put("vecino", repoVecino.getOne(vecinoId));
			m.put("view", "/reserva/cU");
			return "/_t2/frame";
	}

	@PostMapping("c")
	public String cPost(@RequestParam("fecha") String fecha, @RequestParam("franjas[]") String comienzos[],
			@RequestParam("tReserva") Integer tReserva, @RequestParam("vecinoId") String vecinoId,
			@RequestParam("zonaId") Long zonaId) throws DangerException, InfoException {

		if (comienzos.length <= tReserva / 30) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				Date date = formatter.parse(fecha);

				String inicios = "";
				for (int i = 0; i < comienzos.length; i++) {
					inicios += comienzos[i] + ",";
					Franja franja = repoFranja.getByFechaAndHora(date, comienzos[i]);
					franja.setEstado("reservado");
					repoFranja.save(franja);
				}
				Reserva reserva = new Reserva(fecha, inicios.substring(0, inicios.length() - 1), comienzos.length * 30);

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
				PRG.error("Reserva no realizada ", "/vecino/home");
		
			}
			PRG.info("Reserva realizada correctamente", "/vecino/home");
		} else {
			PRG.error("Limite de tiempo para la reserva excedido ", "/vecino/home");
		}

		return "redirect:/vecino/home";
	}

	@RequestMapping(path = "/getFranjas", produces = { "application/json" })
	public @ResponseBody List<String> franjasFecha(@RequestParam("datos") String datos) {

		String[] dato = datos.split("Y");
		String fecha = dato[1];
		List<String> fs = new ArrayList<String>();
		List<Franja> franjas = new ArrayList<Franja>();

		try {

			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateInString = fecha;

			Date date = formatter.parse(dateInString);
			franjas = repoFranja.findByZonaAndFechaAndEstado(repoZonaComun.getOne(Long.parseLong(dato[0])), date,
					"libre");

		} catch (ParseException e) {
			e.printStackTrace();
		}

		for (Franja franja : franjas) {
			fs.add(franja.getHora());
		}

		return fs;

	}
	// =========================================

	@GetMapping("r")
	public String rA(ModelMap m,
			HttpSession s) throws DangerException {
			rol.isRolOK("admin", s);
			List<Reserva> reservas = repoReserva.findAll();
			m.put("reservas", reservas);
			m.put("view", "/reserva/rA");
			return "/_t/frame";
	}
	@GetMapping("rU")
	public String rU (ModelMap m,  
			HttpSession s) throws  DangerException {
			rol.isRolOK("auth", s);
			Vecino v = (Vecino) s.getAttribute("vecino");
			List<Reserva> reservas = repoReserva.findByHace(v);
			m.put("vecino", v);
			m.put("reservas", reservas);
			m.put("view", "/reserva/rU");
			return "/_t2/frame";
	}

	@PostMapping("d")
	public String d(@RequestParam("idR") Long idR, HttpSession s) throws DangerException {
		rol.isRolOK("auth", s);

		try {
			Reserva reserva = repoReserva.getOne(idR);
			String [] inicios = reserva.getInicio().split(",");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

			Date date = formatter.parse(reserva.getFecha());
			for(String inicio : inicios) {
				Franja f = repoFranja.getByFechaAndHora(date, inicio);
				f.setEstado("libre");
			}
			// ==========historico========== Guarda la reserva seleccionada
			helper.historicoReserva(reserva);
			// ==========historico==========
			repoReserva.delete(reserva);
		} catch (Exception e) {
			PRG.error("Error al borrar reserva", "/vecino/home");
		}
		return "redirect:/vecino/home";
	}

	@Scheduled(cron = "0 50 23 * * *", zone = "Europe/Madrid")
	public void funcionAuto2350() {
		Calendar cal = Calendar.getInstance();
		int dia = cal.get(Calendar.DATE);
		int mes = cal.get(Calendar.MONTH);
		int anio = cal.get(Calendar.YEAR);
		cal.set(anio, mes, dia, 0, 0, 0);
		// =================Reservas pendientes-->completadas=====================
		List<Reserva> reservasPend = repoReserva.findByFechaAndEstado(cal.getTime(), "pendiente");
		for (int i = 0; i < reservasPend.size(); i++) {
			reservasPend.get(i).setEstado("completada");
			repoReserva.save(reservasPend.get(i));
		}
		// =================Borrar Franjas de hoy=====================

		List<Franja> franjasHoy = repoFranja.findByFechaAndEstado(cal.getTime(), "libre");
		repoFranja.deleteAll(franjasHoy);

		// =================Añadir Franjas dia pasadas 2 semanas===================== en
		// el controller de zc
		// List<Franja> franjas2Sem= helper.addFranja2sem(zona);

	}
}
