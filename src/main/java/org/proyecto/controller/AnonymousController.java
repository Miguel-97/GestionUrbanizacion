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
import org.proyecto.helper.helper;
import org.proyecto.helper.rol;
import org.proyecto.repository.EdificioRepository;
import org.proyecto.repository.UrbanizacionRepository;
import org.proyecto.repository.VecinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AnonymousController {

	@Autowired
	VecinoRepository repoVecino;

	@Autowired
	EdificioRepository repoEdificio;

	@Autowired
	UrbanizacionRepository repoUrbanizacion;

	// =========================================

	@GetMapping("/")
	public String home(ModelMap m) {
		m.put("view", "/anonymous/home");
		return "/_t/frame";
	}

	// =========================================

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

	// =========================================

	@GetMapping("/init")
	public String initGet(ModelMap m) throws DangerException {
		if (repoVecino.getByUsername("administrador") != null) {
			PRG.error("BD no vacía");
		}
		m.put("view", "/anonymous/init");
		return "/_t/frame";
	}

	@PostMapping("/init")
	public String initPost(@RequestParam("password") String password, ModelMap m) throws DangerException {
		if (repoVecino.getByUsername("administrador") != null) {
			PRG.error("Operación no válida. BD no vacía");
		}
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		if (!bpe.matches(password, (bpe.encode("40m1n157r40or")))) { // Password harcoded
			PRG.error("Contraseña incorrecta", "/init");
		}
		repoVecino.deleteAll();
		repoVecino.save(new Vecino("40m1n157r40or", "administrador", "40m1n157r40or"));
		return "redirect:/";
	}

	// =========================================

	@GetMapping("/login")
	public String login(ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("anon", s);
		m.put("view", "/anonymous/login");
		return "/_t/frame";
	}

	@PostMapping("/login")
	public String loginPost(@RequestParam("username") String usuario, @RequestParam("password") String password,
			ModelMap m, HttpSession s) throws DangerException {
		String view = "/";
		try {
			Vecino vecino = repoVecino.getByUsername(usuario);
			BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
			if (!(bpe.matches(password, vecino.getPassword()))) {
				throw new Exception();
			}
			s.setAttribute("vecino", vecino);
		} catch (Exception e) {
			PRG.error("Usuario o Contraseña incorrecta", "/login");
			view = "/info";
		}
		return "redirect:" + view;
	}

	// =========================================

	@GetMapping("/registro")
	public String registro(ModelMap m) {
		m.put("view", "anonymous/registro");
		m.put("urbanizaciones", repoUrbanizacion.findAll());
		return "/_t/frame";
	}

	@RequestMapping(path = "/getPortales", produces = { "application/json" })
	public @ResponseBody List<Edificio> portalUrba(@RequestParam("urbaId") Long urbaId) {
		List<Edificio> edificios = new ArrayList<>();
		if (urbaId != null) {
			for (int i = 0; i < repoEdificio.findByPerteneceId(urbaId).size(); i++) {
				edificios.add(new Edificio(repoEdificio.findByPerteneceId(urbaId).get(i).getPortal()));
			}
		}
		return edificios;
	}

	@RequestMapping(path = "/getPisosPuertas", produces = { "application/json" })
	public @ResponseBody List<String> pisosPortal(@RequestParam("portal") String portal) {
		List<String> pisosPuertas = new ArrayList<String>();
		if (portal != null) {
			String vecinoId = repoVecino.findByVivePortal(portal).get(0).getId();
			String puerta = vecinoId.substring((vecinoId.length() - 1));
			String nombreUrba [] = vecinoId.split("_");
			Edificio edificio = repoEdificio.getByPerteneceIdAndPortal(repoUrbanizacion.getByNombre(nombreUrba[0]).getId(), portal);
			// EN CASO DE QUE SEAN LETRAS
			ArrayList<Character> letras = helper.denomPuerta(edificio.getPuertasXpiso());
			// EN CASO DE QUE TENGA BAJO Y SEA NUMÉRICO
			if (edificio.getBajo() && helper.isNumeric(puerta)) {
				for (int i = 0; i < edificio.getPisos(); i++) {
					for (int j = 0; j < edificio.getPuertasXpiso(); j++) {
						if (i == 0) {
							pisosPuertas.add("Bajo " + (j + 1));
						} else {
							pisosPuertas.add(i + "º " + (j + 1));
						}
					}
				}
			} 
			else if (!edificio.getBajo() && helper.isNumeric(puerta)) {
				for (int i = 0; i < edificio.getPisos(); i++) {
					for (int j = 0; j < edificio.getPuertasXpiso(); j++) {
						pisosPuertas.add(i + 1 + "º " + (j + 1));
					}
				}
			}
			else if (edificio.getBajo() && !helper.isNumeric(puerta)) {
				for (int i = 0; i < edificio.getPisos(); i++) {
					for (int j = 0; j < edificio.getPuertasXpiso(); j++) {
						if (i == 0) {
							pisosPuertas.add("Bajo " + letras.get(j));
						} else {
							pisosPuertas.add(i + "º " + letras.get(j));
						}					}
				}
			}
			else if (!edificio.getBajo() && !helper.isNumeric(puerta)) {
				for (int i = 0; i < edificio.getPisos(); i++) {
					for (int j = 0; j < edificio.getPuertasXpiso(); j++) {
						pisosPuertas.add(i + 1 + "º " + letras.get(j));
					}
				}
			}
	}

	return pisosPuertas;

	}

	// =========================================

	// Logout va en AuthController
	@GetMapping("/logout")
	public String logout(HttpSession s) throws DangerException {
		rol.isRolOK("auth", s);
		s.invalidate();
		return "redirect:/";
	}
}
