package org.proyecto.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.proyecto.domain.Reserva;
import org.proyecto.domain.Vecino;
import org.proyecto.exception.DangerException;
import org.proyecto.exception.InfoException;
import org.proyecto.helper.PRG;
import org.proyecto.helper.helper;
import org.proyecto.repository.VecinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/vecino")
public class VecinoController {
	@Autowired
	private VecinoRepository repoVecino;

	@GetMapping("r")
	public String r(ModelMap m) {
		List<Vecino> vecinos = repoVecino.findAll();
		m.put("vecinos", vecinos);
		m.put("view", "/vecino/r");
		return "/_t/frame";
	}

	@GetMapping("u")
	public String u(@RequestParam("id") String id, ModelMap m, HttpSession s) throws DangerException {
		m.put("vecino", repoVecino.getOne(id));
		m.put("view", "/vecino/u");
		return "/_t/frame";
	}

	@PostMapping("u")
	public void u(@RequestParam("nombre") String nombre, @RequestParam("email") String email,
			@RequestParam("id") String id, HttpSession s) throws DangerException, InfoException {
		try {
			Vecino v = repoVecino.getOne(id);
			v.setNombre(nombre);
			v.setEmail(email);
			repoVecino.save(v);
		} catch (Exception e) {
			PRG.error("Vecino con email " + email + " ya existente", "/vecino/r");
		}
		PRG.info("Vecino " + nombre + " actualizado correctamente", "/vecino/r");
	}

	@PostMapping("d")
	public String d(@RequestParam("idV") String idV) throws DangerException {
		String idVecino = "";
		try {
			Vecino vecino = repoVecino.getOne(idV);
			// ==========historico==========
			// Guarda el vecino y las reservas realizadas por este vecino
			ArrayList<Reserva> reservas = new ArrayList<>();
			reservas.addAll(vecino.getReservas());
			helper.historicoVecino(vecino, reservas);
			System.out.println(helper.leerArchivo("vecinos"));
			// ==========historico==========

			idVecino = vecino.getId();
			repoVecino.delete(vecino);
		} catch (Exception e) {
			PRG.error("Error al borrar el vecino " + idVecino, "/vecino/r");
		}
		return "redirect:/vecino/r";
	}
}