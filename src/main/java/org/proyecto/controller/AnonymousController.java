package org.proyecto.controller;

import javax.servlet.http.HttpSession;
import org.proyecto.domain.Vecino;
import org.proyecto.exception.DangerException;
import org.proyecto.exception.InfoException;
import org.proyecto.helper.PRG;
import org.proyecto.helper.rol;
import org.proyecto.repository.EdificioRepository;
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
	public String registro(HttpSession s, ModelMap m) throws DangerException {
		rol.isRolOK("anon", s);
		m.put("view", "anonymous/registro");
		m.put("urbas", repoUrbanizacion.findAll());
		m.put("vecinos", repoVecino.findByEstado("pendiente"));
		return "/_t/frame";
	}
	
	@RequestMapping("/registro")
	public @ResponseBody String registroPost(
			@RequestParam("urbaId") Long urbaId,
			@RequestParam("portal") String portal, 
			@RequestParam("piso") String piso,
			@RequestParam("puerta") String puerta,
			@RequestParam("email") String email
			) throws InfoException{
	
		String edificios = "", puertas="", pisos="";
		
		if(urbaId!=null) {
			for(int i=0;i<repoEdificio.findByPerteneceId(urbaId).size();i++) {
				edificios += repoEdificio.findByPerteneceId(urbaId).get(i).getPortal() + ",";
			}
			return edificios;
		}
		if(portal!=null) {
			for(int i=0;i<repoEdificio.findByPerteneceIdAndPortal(urbaId, portal).size();i++) {
				pisos += repoEdificio.findByPerteneceIdAndPortal(urbaId, portal).get(i).getPisos();
			}
			return pisos;
		}
		
		return "redirect:/anonymous/login";
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
