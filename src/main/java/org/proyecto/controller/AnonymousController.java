package org.proyecto.controller;

import javax.servlet.http.HttpSession;
import org.apache.naming.factory.SendMailFactory;
import org.proyecto.domain.Vecino;
import org.proyecto.exception.DangerException;
import org.proyecto.helper.PRG;
import org.proyecto.helper.rol;
import org.proyecto.repository.VecinoRepository;
import org.proyecto.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AnonymousController {

	@Autowired
	VecinoRepository repoVecino;

	@Autowired
	private MailService mailService;

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

	// HAY QUE CAMBIARLO
	/*
	 * @GetMapping("/registro") public String registro(ModelMap m, HttpSession s)
	 * throws DangerException { rol.isRolOK("anon", s); m.put("view",
	 * "/anonymous/registro"); return "/_t/frame"; }
	 * 
	 * @PostMapping("registro") public String registroPost(
	 * 
	 * @RequestParam("urbaId") Long urbaId,
	 * 
	 * @RequestParam(value = "portal", required = false) Integer portal,
	 * 
	 * @RequestParam(value = "piso", required = false) String piso,
	 * 
	 * @RequestParam(value = "puerta", required = false) String puerta,
	 * 
	 * @RequestParam("nombre") String nombre,
	 * 
	 * @RequestParam("emailUsuario") String emailUsuario) throws DangerException{
	 * try { //Enviar el email al usuario String asunto="App Gestión Urbanización";
	 * String mensaje="Gracias por tu registro,\r\n" +
	 * "Estas son tus credenciales para acceder a la app:\r\n" +
	 * "Nombre de usuario:       Contraseña: \r\n" + "Gracias.\r\n";
	 * 
	 * mailService.sendMail("gestion.urbanizacion.2020@gmail.com",emailUsuario,
	 * asunto,mensaje); //Guardar datos en la bbdd } catch(Exception e) {
	 * PRG.error("Error al registrar el vecino", "/vecino/r"); }
	 * 
	 * return "redirect:/vecino/r"; }
	 */

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

	// Logout va en AuthController
	@GetMapping("/logout")
	public String logout(HttpSession s) throws DangerException {
		rol.isRolOK("auth", s);
		s.invalidate();
		return "redirect:/";
	}
}
