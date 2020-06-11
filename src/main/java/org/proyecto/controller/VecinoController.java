package org.proyecto.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.proyecto.domain.Reserva;
import org.proyecto.domain.Urbanizacion;
import org.proyecto.domain.Vecino;
import org.proyecto.domain.ZonaComun;
import org.proyecto.exception.DangerException;
import org.proyecto.exception.InfoException;
import org.proyecto.helper.PRG;
import org.proyecto.helper.helper;
import org.proyecto.helper.rol;
import org.proyecto.repository.EdificioRepository;
import org.proyecto.repository.ReservaRepository;
import org.proyecto.repository.UrbanizacionRepository;
import org.proyecto.repository.VecinoRepository;
import org.proyecto.repository.ZonaComunRepository;
import org.proyecto.service.MailService;
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
	@Autowired
	private EdificioRepository repoEdificio;
	@Autowired
	private ZonaComunRepository repoZona;
	@Autowired
	private UrbanizacionRepository repoUrba;
	@Autowired
	private ReservaRepository repoReserva;

	@Autowired
	private MailService mailService;

	// =========================================

	@GetMapping("r")
	public String r(ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("administrador", s);
		List<Vecino> vecinos = repoVecino.findAll();
		m.put("vecinos", vecinos);
		m.put("view", "/vecino/r");
		return "/_t/frame";
	}

	// =========================================
	// EDITAR PERFIL USUARIO (USERNAME Y PASSWORD)
	@GetMapping("u")
	public String u(@RequestParam("id") String id, ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("auth", s);
		m.put("vecino", repoVecino.getOne(id));
		m.put("view", "/vecino/u");
		return "/_t2/frame";
	}

	@PostMapping("u")
	public void u(@RequestParam(value = "username", required = false) String username,
			@RequestParam("password") String pwd, @RequestParam("id") String id, HttpSession s)
			throws DangerException, InfoException {
		if (pwd == null || pwd.trim().equals("")) {
			PRG.error("No puede estar la contraseña vacía", "/vecino/home");
		} else {
			try {
				Vecino v = repoVecino.getOne(id);
				if (username == null || username.trim().equals("")) {
					v.setUsername(v.getId());
				} else {
					v.setUsername(username);
				}
				v.setPassword(pwd);

				repoVecino.save(v);
				s.setAttribute("vecino", v);
			} catch (Exception e) {
				PRG.error("Perfil no actualizado", "/vecino/home");
			}
		}
		PRG.info("Perfil actualizado correctamente", "/vecino/home");
	}
	//UPDATE DE TODOS LOS VECINOS DE UN EDIFICIO (PARTE ADMIN)
	@GetMapping("ul")
	public String ul(@RequestParam("idE") Long idEdificio, ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("administrador", s);
		m.put("vecinos", repoVecino.findByViveId(idEdificio));
		m.put("portal", repoEdificio.getOne(idEdificio).getPortal());
		m.put("view", "/vecino/ul");
		return "/_t/frame";
	}

	@PostMapping("ul")
	public void ul(@RequestParam("portal") String portal, @RequestParam("ids[]") String[] ids,
			@RequestParam("nombres[]") String[] nombres, @RequestParam("emails[]") String[] emails, HttpSession s)
			throws DangerException, InfoException {
		try {
			if (ids.length == 0) {
				throw new Exception("No hay vecinos para actualizar");
			} else {
				int i = 0;
				while (i < ids.length) {
					if (!nombres[i].isEmpty() || !emails[i].isEmpty()) {
						Vecino v = repoVecino.getOne(ids[i]);
						if (!nombres[i].isEmpty()) {
							v.setNombre(nombres[i]);
						}
						if (!emails[i].isEmpty()) {
							v.setEmail(emails[i]);
							if (v.getEstado().equals("inactivo")) {

								String asunto = "Registro UrbaZone";
								String mensaje = "¡Hola vecino!\n"
										+ "Estamos encantados de darte la bienvenida a UrbaZone.\n\n"
										+ "Si no enviaste la solicitud, no es necesario que realices ninguna acción. Simplemente, omite el mensaje; no se verificará la cuenta.\n"
										+ "Recuerda cambiar el nombre de usuario y la contraseña en el primer inicio.\n"
										+ "Estas son sus credenciales para acceder:\n" + "           Usuario: "
										+ emails[i] + "           Contraseña: " + v.getPassword() + "\n\n"
										+ "Gracias por unirse a nosotros.\n" + "Su equipo de UrbaZone.\n\n"
										+ "Este mensaje va dirigido, de manera exclusiva, a su destinatario y puede contener información confidencial y sujeta al secreto profesional, cuya divulgación no está permitida por Ley. En caso de haber recibido este mensaje por error, le rogamos que de forma inmediata, nos lo comunique mediante correo electrónico remitido a nuestra atención y proceda a su eliminación, así como a la de cualquier documento adjunto al mismo. Asimismo, le comunicamos que la distribución, copia o utilización de este mensaje, o de cualquier documento adjunto al mismo, cualquiera que fuera su finalidad, están prohibidas por la ley. En aras del cumplimiento del Reglamento (UE) 2016/679 del Parlamento Europeo y del Consejo, de 27 de abril de 2016, puede ejercer los derechos de acceso, rectificación, cancelación, limitación, oposición y portabilidad de manera gratuita mediante correo electrónico a: gestion.urbanizacion.2020@gmail.com";
								mailService.sendMail("gestion.urbanizacion.2020@gmail.com", emails[i], asunto, mensaje);
								v.setEstado("pendiente");
							}
						}

						repoVecino.save(v);
						i++;
					} else {
						i++;
					}
				}
			}
		} catch (Exception e) {
			PRG.error("Error al actualizar los vecinos del portal " + portal, "/edificio/r");
		}
		PRG.info("Vecinos del portal " + portal + " actualizados correctamente", "/vecino/r");
	}

	// =========================================

	@PostMapping("d")
	public String d(@RequestParam("idV") String idV, HttpSession s) throws DangerException {
		rol.isRolOK("administrador", s);
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

	// =========================================
	//HOME DEL USUARIO
	@GetMapping("/home")
	public String homeUsuario(ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("auth", s);
		Vecino vecino = (Vecino) s.getAttribute("vecino");
		String idVecino[] = vecino.getId().split("_");
		Urbanizacion urba = repoUrba.getByNombre(idVecino[0]);
		m.put("zonas", repoZona.findByCorresponde(urba));
		m.put("view", "/vecino/homeUsuario");

		return "/_t2/frame";
	}
	//PERFIL DEL USUARIO
	@GetMapping("/perfil")
	public String miPerfil(ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("auth", s);
		m.put("view", "/vecino/perfilUsuario");
		return "/_t2/frame";
	}

	@GetMapping("/estadistica")
	public String estadistica(ModelMap m, HttpSession s) throws DangerException {
		rol.isRolOK("auth", s);
		Vecino vecino = (Vecino) s.getAttribute("vecino");
		// Reservas realizadas
		List<Reserva> resVecino = repoReserva.findByHace(vecino);
		m.put("nReservas", resVecino.size());

		// Minutos reservados totales
		int tResTot = 0;
		for (Reserva res : resVecino) {
			tResTot += (res.gettReserva());
		}
		m.put("tResTot", tResTot);
		// Franjas reservadas totales
	
		m.put("fResTot", tResTot / 30);

	//TODO
		
		List<ZonaComun> zonas = new ArrayList<ZonaComun>();	
		List<Reserva> resZona = new ArrayList<>();
		//List<String> zonaMin = new ArrayList<String>();
		//int numResZ = 0;
		for (Reserva res : resVecino) {
			zonas.add(res.getTiene());
			resZona.addAll(repoReserva.findByTiene(res.getTiene()));
		}
		// zonaMin.add(zona.getNombre()+"nres");

		// Zona más reservada
		// Horas reservada en zonas

		// zonaMr=repoZona.getOne(repoReserva.).getNombre; m.put("zn", value);

		// m.put("nBloquesT", );

		m.put("reservas", resVecino);
		m.put("view", "/vecino/estadistica");
		return "/_t2/frame";
	}

}