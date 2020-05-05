package org.proyecto.controller;

import javax.servlet.http.HttpSession;

import org.proyecto.domain.Vecino;
import org.proyecto.exception.DangerException;
import org.proyecto.helper.PRG;
import org.proyecto.repository.VecinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
	@Autowired
	VecinoRepository repoVecino;

	@GetMapping("/")
	public String home(ModelMap m) {
		m.put("view", "/home/home");
		return "/_t/frame";
	}

	@GetMapping("/info")
	public String info(HttpSession s, ModelMap m) {
		String mensaje = s.getAttribute("_mensaje") != null ? (String) s.getAttribute("_mensaje")
				: "Pulsa para volver a home";
		String severity = s.getAttribute("_severity") != null ? (String) s.getAttribute("_severity") : "info";
		String link = s.getAttribute("_link") != null ? (String) s.getAttribute("_link") : "/";

		s.removeAttribute("_mensaje");
		s.removeAttribute("_severity");
		s.removeAttribute("_link");

		m.put("mensaje", mensaje);
		m.put("severity", severity);
		m.put("link", link);

		m.put("view", "/_t/info");
		return "/_t/frame";
	}

	@GetMapping("/init")
	public String initGet(ModelMap m) throws DangerException {
		if (repoVecino.getByUsername("administrador") != null) {
			PRG.error("BD no vacía");
		}
		m.put("view", "/home/init");
		return "/_t/frame";
	}
	
	@PostMapping("/init")
	public String initPost(@RequestParam("password") String password, ModelMap m) throws DangerException {
		if (repoVecino.getByUsername("administrador") != null) {
			PRG.error("Operación no válida. BD no vacía");
		}
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		if (!bpe.matches(password, bpe.encode("40m1n157r40or"))) { // Password harcoded
			PRG.error("Contraseña incorrecta", "/init");
		}
		repoVecino.deleteAll();
		repoVecino.save(new Vecino("40m1n157r40or", "administrador", bpe.encode("40m1n157r40or")));
		return "redirect:/";
	}

}
