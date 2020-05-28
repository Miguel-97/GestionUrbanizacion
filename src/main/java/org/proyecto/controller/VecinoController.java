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
import org.proyecto.repository.EdificioRepository;
import org.proyecto.repository.VecinoRepository;
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
	private MailService mailService;

	// =========================================

	@GetMapping("r")
	public String r(ModelMap m) {
		List<Vecino> vecinos = repoVecino.findAll();
		m.put("vecinos", vecinos);
		m.put("view", "/vecino/r");
		return "/_t/frame";
	}

	// =========================================

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
			v.setEstado("pendiente");
			repoVecino.save(v);
			
			//Envía Correo
			if(v.getEstado().equals("inactivo")) {
				String asunto="Login UrbaZone";
				String mensaje = "¡Hola vecino!\n"
						+ "Estamos encantados de darte la bienvenida a UrbaZone.\n\n"
						+ "Si no enviaste la solicitud, no es necesario que realices ninguna acción. Simplemente, omite el mensaje; no se verificará la cuenta.\n"
						+ "Recuerda cambiar el nombre de usuario y la contraseña en el primer inicio.\n"
						+ "Estas son sus credenciales para acceder:\n"
						+ "           Usuario: " + email + "           Contraseña: " + v.getPassword() + "\n\n"
						+ "Gracias por unirse a nosotros.\n"
						+ "Su equipo de UrbaZone.\n\n"
						+ "Este mensaje va dirigido, de manera exclusiva, a su destinatario y puede contener información confidencial y sujeta al secreto profesional, cuya divulgación no está permitida por Ley. En caso de haber recibido este mensaje por error, le rogamos que de forma inmediata, nos lo comunique mediante correo electrónico remitido a nuestra atención y proceda a su eliminación, así como a la de cualquier documento adjunto al mismo. Asimismo, le comunicamos que la distribución, copia o utilización de este mensaje, o de cualquier documento adjunto al mismo, cualquiera que fuera su finalidad, están prohibidas por la ley. En aras del cumplimiento del Reglamento (UE) 2016/679 del Parlamento Europeo y del Consejo, de 27 de abril de 2016, puede ejercer los derechos de acceso, rectificación, cancelación, limitación, oposición y portabilidad de manera gratuita mediante correo electrónico a: gestion.urbanizacion.2020@gmail.com";
				}
			
			
		} catch (Exception e) {
			PRG.error("Vecino con email " + email + " ya existente", "/vecino/r");
		}
		PRG.info("Vecino " + nombre + " actualizado correctamente", "/vecino/r");
	}

	@GetMapping("ul")
	public String ul(@RequestParam("idE") Long idEdificio, ModelMap m, HttpSession s) throws DangerException {
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
							if(v.getEstado().equals("inactivo")) {
								
								String asunto="Registro UrbaZone";
								String mensaje = "¡Hola vecino!\n"
										+ "Estamos encantados de darte la bienvenida a UrbaZone.\n\n"
										+ "Si no enviaste la solicitud, no es necesario que realices ninguna acción. Simplemente, omite el mensaje; no se verificará la cuenta.\n"
										+ "Recuerda cambiar el nombre de usuario y la contraseña en el primer inicio.\n"
										+ "Estas son sus credenciales para acceder:\n"
										+ "           Usuario: " + emails[i] + "           Contraseña: " + v.getPassword() + "\n\n"
										+ "Gracias por unirse a nosotros.\n"
										+ "Su equipo de UrbaZone.\n\n"
										+ "Este mensaje va dirigido, de manera exclusiva, a su destinatario y puede contener información confidencial y sujeta al secreto profesional, cuya divulgación no está permitida por Ley. En caso de haber recibido este mensaje por error, le rogamos que de forma inmediata, nos lo comunique mediante correo electrónico remitido a nuestra atención y proceda a su eliminación, así como a la de cualquier documento adjunto al mismo. Asimismo, le comunicamos que la distribución, copia o utilización de este mensaje, o de cualquier documento adjunto al mismo, cualquiera que fuera su finalidad, están prohibidas por la ley. En aras del cumplimiento del Reglamento (UE) 2016/679 del Parlamento Europeo y del Consejo, de 27 de abril de 2016, puede ejercer los derechos de acceso, rectificación, cancelación, limitación, oposición y portabilidad de manera gratuita mediante correo electrónico a: gestion.urbanizacion.2020@gmail.com";
								mailService.sendMail("gestion.urbanizacion.2020@gmail.com",emails[i],asunto,mensaje);
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