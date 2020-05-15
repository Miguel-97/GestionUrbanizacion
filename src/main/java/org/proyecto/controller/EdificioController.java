package org.proyecto.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.proyecto.domain.Edificio;
import org.proyecto.domain.Reserva;
import org.proyecto.domain.Urbanizacion;
import org.proyecto.domain.Vecino;
import org.proyecto.exception.DangerException;
import org.proyecto.exception.InfoException;
import org.proyecto.helper.PRG;
import org.proyecto.helper.helper;
import org.proyecto.repository.EdificioRepository;
import org.proyecto.repository.UrbanizacionRepository;
import org.proyecto.repository.VecinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/edificio")
public class EdificioController {
	@Autowired
	private UrbanizacionRepository repoUrbanizacion;
	@Autowired
	private EdificioRepository repoEdificio;
	@Autowired
	private VecinoRepository repoVecino;

	@GetMapping("c")
	public String crearGet(ModelMap m, HttpSession s) throws DangerException {
		m.put("urbanizaciones", repoUrbanizacion.findAll());
		m.put("view", "/edificio/c");
		return "/_t/frame";
	}

	@PostMapping("c")
	public String crearPost(@RequestParam("portal") String portal, @RequestParam("pisos") Integer pisos,
			@RequestParam("puertasXpiso") Integer puertasXpiso, @RequestParam("denominacion") String denominacion,
			@RequestParam("bajo") String bajo, @RequestParam("urbaId") Long urbaId)
			throws DangerException, InfoException {

		if (repoEdificio.getByPortal(portal) != null) {// Comprueba que no exista el portal a crear
			PRG.error("Portal " + portal + "ya existente en la urbanizacion", "/edificio/c");
		} else {
			if (portal == null || pisos == null || pisos < 0 || puertasXpiso == null || puertasXpiso < 0
					|| urbaId == null) {// comprueba que no haya
				// campos vacios o negativos
				PRG.error("Datos vacios y/o negativos, rellene los datos correctamente", "/edificio/c");
			} else {
				try {
					Edificio edificio = new Edificio(portal, pisos, puertasXpiso);
					Urbanizacion urbanizacion = repoUrbanizacion.getOne(urbaId);

					urbanizacion.getEdificios().add(edificio);
					edificio.setPertenece(urbanizacion);
					repoEdificio.save(edificio);

					try {
						Edificio edi = repoEdificio.getOne(edificio.getId());

						ArrayList<Character> letras = helper.denomPuerta(puertasXpiso);
						String id = "", username = "", password = "";
						ArrayList<Vecino> vecindad = new ArrayList<>();
						for (int j = 0; j < pisos; j++) { // recorre los pisos
							for (int i = 0; i < puertasXpiso; i++) { // recorre las puertas por piso
								if (bajo.equals("si")) {// comprueba si hay bajo en el edificio o no
									if (denominacion.equals("numeros")) {// comprueba si la denominacion de las puertas
																			// por piso del edificio son letras o
																			// numeros
										if (j == 0) {// Comprueba si esta en la planta baja para añadir los datos como
														// bajo
											id = urbanizacion.getNombre() + "_" + portal + "_Bajo_" + (i + 1);
											username = urbanizacion.getNombre() + "_" + portal + "_Bajo_" + (i + 1);
										} else {
											id = urbanizacion.getNombre() + "_" + portal + "_" + j + "_" + (i + 1);
											username = urbanizacion.getNombre() + "_" + portal + "_" + j + "_"
													+ (i + 1);
										}
									} else {
										if (j == 0) {
											id = urbanizacion.getNombre() + "_" + portal + "_Bajo_" + letras.get(i);
											username = urbanizacion.getNombre() + "_" + portal + "_Bajo_"
													+ letras.get(i);
										} else {
											id = urbanizacion.getNombre() + "_" + portal + "_" + j + "_"
													+ letras.get(i);
											username = urbanizacion.getNombre() + "_" + portal + "_" + j + "_"
													+ letras.get(i);
										}
									}
								} else {
									if (denominacion.equals("numeros")) {
										id = urbanizacion.getNombre() + "_" + portal + "_" + (j + 1) + "_" + (i + 1);
										username = urbanizacion.getNombre() + "_" + portal + "_" + (j + 1) + "_"
												+ (i + 1);
									} else {
										id = urbanizacion.getNombre() + "_" + portal + "_" + (j + 1) + "_"
												+ letras.get(i);
										username = urbanizacion.getNombre() + "_" + portal + "_" + (j + 1) + "_"
												+ letras.get(i);
									}
								}
								password = helper.generadorPassword();
								Vecino vecino = new Vecino(id, username, password);
								vecindad.add(vecino);
								vecino.setVive(edi);
								repoVecino.save(vecino);
							}
						}
						edi.getVecinos().addAll(vecindad);
						repoEdificio.save(edi);
					} catch (Exception e) {
						PRG.error("Vecino no creado", "/edificio/c");
					}

				} catch (Exception e) {

					PRG.error("Edificio no creado", "/edificio/c");
				}
			}
		}

		return "redirect:/edificio/r";
	}

	@GetMapping("r")
	public String r(ModelMap m) {
		List<Edificio> edificios = repoEdificio.findAll();
		m.put("edificios", edificios);
		m.put("view", "/edificio/r");
		return "/_t/frame";
	}

	@PostMapping("d")
	public String d(@RequestParam("idE") Long idE) throws DangerException {
		String portalEdificio = "";
		try {
			Edificio edificio = repoEdificio.getOne(idE);

			// ==========historico==========
			// Guarda los vecinos del edificio y las reservas realizadas por estos vecinos
			ArrayList<Vecino> vecinos = new ArrayList<>();
			ArrayList<Reserva> reservas = new ArrayList<>();
			vecinos.addAll(edificio.getVecinos());
			for (Vecino vecino : vecinos) {
				reservas.addAll(vecino.getReservas());
			}
			helper.historicoEdificio(edificio, vecinos, reservas);
			System.out.println(helper.leerArchivo("edificios"));
			System.out.println(helper.leerArchivo("vecinos"));
			System.out.println(helper.leerArchivo("reservas"));
			// ==========historico==========

			portalEdificio = edificio.getPortal();
			repoEdificio.delete(edificio);
		} catch (Exception e) {
			PRG.error("Error al borrar el portal " + portalEdificio, "/edificio/r");
		}
		return "redirect:/edificio/r";
	}

}