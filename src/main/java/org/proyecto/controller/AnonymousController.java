package org.proyecto.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.proyecto.domain.Edificio;
import org.proyecto.domain.Reserva;
import org.proyecto.domain.Vecino;
import org.proyecto.exception.DangerException;
import org.proyecto.helper.PRG;
import org.proyecto.helper.helper;
import org.proyecto.helper.rol;
import org.proyecto.repository.EdificioRepository;
import org.proyecto.repository.ReservaRepository;
import org.proyecto.repository.UrbanizacionRepository;
import org.proyecto.repository.VecinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

	@Autowired
	ReservaRepository repoReserva;

	// =========================================

	@GetMapping("/")
	public String home(ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("anon", s);
		return "redirect:/login";
	}

	// =========================================

	@GetMapping("/privacidad")
	public String privacidad(ModelMap m, HttpSession s) {
		m.put("view", "/anonymous/privacidad");
		return "/_t/frame";
	}
	// =========================================

	@GetMapping("/cookies")
	public String cookies(ModelMap m, HttpSession s) {
		m.put("view", "/anonymous/cookies");
		return "/_t/frame";
	}
	// =========================================

	@GetMapping("/homeAdmin")
	public String homeAdmin(ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("administrador", s);
		m.put("view", "/anonymous/homeAdmin");
		return "/_t3/frame";
	}

	@GetMapping("/estadistica")
	public String estadisticasAdmin(ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("administrador", s);
		// Reservas realizadas
		List<Reserva> reservasT = repoReserva.findAll();
		m.put("nReservas", reservasT.size());

		// Minutos reservados totales
		int tResTot = 0;
		for (Reserva res : reservasT) {
			tResTot += (res.gettReserva());
		}
		m.put("tResTot", tResTot);
		// Franjas reservadas totales
		m.put("fResTot", tResTot / 30);

		m.put("reservas", reservasT);
		m.put("view", "/anonymous/estadistica");
		return "/_t3/frame";
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
	public String initGet(ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("anon", s);
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
		repoVecino.save(new Vecino("40m1n157r40or", "administrador", "administrador", "40m1n157r40or",
				"gestion.urbanizacion.2020@gmail.com", "administrador"));
		return "redirect:/login";
	}

	// =========================================

	@GetMapping("/login")
	public String login(ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("anon", s);
		m.put("view", "/anonymous/login");
		return "/_t/frame";
	}

	@PostMapping("/login")
	public String loginPost(@RequestParam("email") String email, @RequestParam("password") String password, ModelMap m,
			HttpSession s) throws DangerException {
		String view = "/";
		try {
			Vecino vecino = repoVecino.getByEmail(email);
			if (vecino.getEmail().equals("gestion.urbanizacion.2020@gmail.com")
					&& vecino.getPassword().equals(password)) {
				vecino.setEstado("activo");
				s.setAttribute("vecino", vecino);
				view = "/homeAdmin";
			} else {
				BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
				if (vecino.getEstado().equals("pendiente")) {
					if (vecino.getPassword().equals(password)) {
						vecino.setEstado("activo");
						vecino.setPassword(password);
						repoVecino.save(vecino);
						s.setAttribute("vecino", vecino);
						s.setAttribute("inicio", "primero");
						view = "/vecino/home";
					} else {
						PRG.error("Email o Contraseña incorrecta", "/login");
						view = "/info";
					}
				} else if (vecino.getEstado().equals("activo")) {
					if ((bpe.matches(password, vecino.getPassword()))) {
						s.setAttribute("vecino", vecino);
						s.setAttribute("inicio", "multiple");
						view = "/vecino/home";
					} else {
						PRG.error("Usuario o Contraseña incorrecta", "/login");
						view = "/info";
					}
				}
			}
		} catch (Exception e) {
			PRG.error("Email o contraseña incorrecta", "/login");
			view = "/info";
		}
		return "redirect:" + view;
	}

	// =========================================

	@GetMapping("/registro")
	public String registro(ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("anon", s);
		m.put("view", "/anonymous/registro");
		m.put("urbanizaciones", repoUrbanizacion.findAll());
		return "/_t/frame";
	}

	// CARGA LOS PORTALES AL SELECCIONAR URBANIZACIÓN
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

	// CARGA LOS PISOS Y LAS PUERTAS AL SELECCIONAR PORTAL
	@RequestMapping(path = "/getPisosPuertas", produces = { "application/json" })
	public @ResponseBody List<String> pisosPortal(@RequestParam("dato") String dato) {
		String datos[] = dato.split("Y");
		List<String> pisosPuertas = new ArrayList<String>();
		if (datos[0] != null) {
			Edificio edificio = repoEdificio.getByPerteneceIdAndPortal(Long.parseLong(datos[1]), datos[0]);
			String vecinoId = repoVecino.findByVive(edificio).get(0).getId();
			String puertas[] = vecinoId.split("_");
			String puerta = puertas[3];
			ArrayList<Character> letras = helper.denomPuerta(edificio.getPuertasXpiso());
			// EN CASO DE QUE TENGA BAJO Y SEA NUMÉRICO
			if (edificio.getBajo() && helper.isNumeric(puerta)) {
				for (int i = 0; i < edificio.getPisos(); i++) {
					for (int j = 0; j < edificio.getPuertasXpiso(); j++) {
						if (i == 0) {
							pisosPuertas.add("Bajo-" + (j + 1));
						} else {
							pisosPuertas.add(i + "-" + (j + 1));
						}
					}
				}
			}
			// EN CASO DE QUE NO TENGA BAJO Y SEA NUMÉRICO
			else if (!edificio.getBajo() && helper.isNumeric(puerta)) {
				for (int i = 0; i < edificio.getPisos(); i++) {
					for (int j = 0; j < edificio.getPuertasXpiso(); j++) {
						pisosPuertas.add(i + 1 + "-" + (j + 1));
					}
				}
			}
			// EN CASO DE QUE TENGA BAJO Y SEAN LETRAS
			else if (edificio.getBajo() && !helper.isNumeric(puerta)) {
				for (int i = 0; i < edificio.getPisos(); i++) {
					for (int j = 0; j < edificio.getPuertasXpiso(); j++) {
						if (i == 0) {
							pisosPuertas.add("Bajo-" + letras.get(j));
						} else {
							pisosPuertas.add(i + "-" + letras.get(j));
						}
					}
				}
			}
			// EN CASO DE QUE NO TENGA BAJO Y SEAN LETRAS
			else if (!edificio.getBajo() && !helper.isNumeric(puerta)) {
				for (int i = 0; i < edificio.getPisos(); i++) {
					for (int j = 0; j < edificio.getPuertasXpiso(); j++) {
						pisosPuertas.add(i + 1 + "-" + letras.get(j));
					}
				}
			}
		}
		return pisosPuertas;
	}

	@PostMapping("/registro")
	public String registroPost(@RequestParam("urbaId") Long urbaId, @RequestParam("portal") String portal,
			@RequestParam("pisosPuertas") String pisosPuertas, @RequestParam("email") String email)
			throws DangerException {
		String view = "/";
		String pisoPuerta[] = pisosPuertas.split("-");
		String vecinoId = repoUrbanizacion.getOne(urbaId).getNombre() + "_" + portal + "_" + pisoPuerta[0] + "_"
				+ pisoPuerta[1];
		Vecino vecino = repoVecino.getOne(vecinoId);
		if (vecino.getEmail() != null && vecino.getEmail().equals(email)) {
			vecino.setEstado("pendiente");
			repoVecino.save(vecino);
			view = "/login";
		} else {
			PRG.error(
					"Los datos no existen en nuestra base de datos, prueba de nuevo o comuníquese con su administrador de la urbanización",
					"/registro");
			view = "/info";
		}
		return "redirect:" + view;
	}

	// =========================================
}
