package org.proyecto.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;
import org.proyecto.domain.Edificio;
import org.proyecto.domain.Franja;
import org.proyecto.domain.Reserva;
import org.proyecto.domain.Urbanizacion;
import org.proyecto.domain.Vecino;
import org.proyecto.domain.ZonaComun;

public class helper {

	public static boolean isNumeric(String cadena) {

		boolean resultado;

		try {
			Integer.parseInt(cadena);
			resultado = true;
		} catch (NumberFormatException excepcion) {
			resultado = false;
		}

		return resultado;
	}

	// ==================================================

	public static String cadenaLetrasMayMin(String cadena) {
		String nuevaCadena = "";
		for (String palabra : cadena.split(" ")) {
			nuevaCadena += palabra.substring(0, 1).toUpperCase() + palabra.substring(1, palabra.length()).toLowerCase()
					+ " ";
		}
		nuevaCadena = nuevaCadena.trim();
		return nuevaCadena;
	}

	// ==================================================

	public static String generadorPassword() {
		char[] validas = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
		StringBuilder password = new StringBuilder(10);
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			char c = validas[random.nextInt(validas.length)];
			password.append(c);
		}
		return password.toString();
	}

	// =================DENOMINACION PUERTA=================
	public static ArrayList<Character> denomPuerta(Integer puertasXpiso) {

		ArrayList<Character> letras = new ArrayList<Character>();
		for (int i = 0; i < puertasXpiso; i++) {
			letras.add((char) (65 + i));
		}
		return letras;

	}

	// =================FRANJAS=================
	public static Collection<Franja> inicializarFranjas(ZonaComun zona) {
		// TODO comprobar tema de la hora 00:00

		Calendar cal = Calendar.getInstance();
		int dia = cal.get(Calendar.DATE);
		int mes = cal.get(Calendar.MONTH);
		int anio = cal.get(Calendar.YEAR);

		Collection<Franja> franjas = new ArrayList<>();
		String temp[] = zona.getHorario().split("-");// {temp[0]--> [hh:mm] temp[1]-->[hh:mm]}-->String

		String[] horaminI = temp[0].split(":"); // [hh:mm]
		String[] horaminF = temp[1].split(":"); // [hh:mm]

		int horaI = Integer.parseInt(horaminI[0]);// [hh]
		int minI = Integer.parseInt(horaminI[1]);// [mm]
		int horaF = Integer.parseInt(horaminF[0]);// [hh]
		int minF = Integer.parseInt(horaminF[1]);// [mm]
		for (int dias = 0; dias < 14; dias++) {
			cal.set(anio, mes, dia, 0, 0, 0);
			cal.add(Calendar.DAY_OF_YEAR, dias);
			int franjasDiarias = 0;
			if (minI == minF) {// 1
				franjasDiarias = (horaF - horaI) * 2;
			} else if (minI == 00 && minF == 30) {// 2
				franjasDiarias = ((horaF - horaI) * 2) + 1;
			} else if (minI == 30 && minF == 00) {// 3
				franjasDiarias = ((horaF - horaI) * 2) - 1;
			}
			for (int i = 0; i <= franjasDiarias; i++) {
				Franja f = new Franja();
				if (minI == 00 && i % 2 == 0) {// a en puntos minI ==0
					int hora = horaI + (i / 2);
					f.setHora("" + hora + ":00");
				} else if (minI == 00 && i % 2 != 0) {// y medias minI ==0
					int hora = horaI + (i / 2);
					f.setHora("" + hora + ":30");
				} else if (minI == 30 && i % 2 == 0) {// a y medias minI ==30
					int hora = horaI + (i / 2);
					f.setHora("" + hora + ":30");
				} else if (minI == 30 && i % 2 != 0) {// en puntos minI ==30
					int hora = horaI + ((i / 2) + 1);
					f.setHora("" + hora + ":00");
				}
				f.setFecha(cal.getTime());
				f.setZona(zona);
				franjas.add(f);
			}
		}
		return franjas;
	}

	public static Collection<Franja> addFranja2sem(ZonaComun zona) {

		Calendar fecha2sem = Calendar.getInstance();
		int dia = fecha2sem.get(Calendar.DATE);
		int mes = fecha2sem.get(Calendar.MONTH);
		int anio = fecha2sem.get(Calendar.YEAR);

		Collection<Franja> franjas2sem = new ArrayList<>();

		fecha2sem.set(anio, mes, dia, 0, 0, 0);
		fecha2sem.add(Calendar.DAY_OF_YEAR, 14);

		String temp[] = zona.getHorario().split("-");// {temp[0]--> [hh:mm] temp[1]-->[hh:mm]}-->String

		String[] horaminI = temp[0].split(":"); // [hh:mm]
		String[] horaminF = temp[1].split(":"); // [hh:mm]

		int horaI = Integer.parseInt(horaminI[0]);// [hh]
		int minI = Integer.parseInt(horaminI[1]);// [mm]
		int horaF = Integer.parseInt(horaminF[0]);// [hh]
		int minF = Integer.parseInt(horaminF[1]);// [mm]
		int franjasDiarias = 0;

		if (minI == minF) {// 1
			franjasDiarias = (horaF - horaI) * 2;
		} else if (minI == 00 && minF == 30) {// 2
			franjasDiarias = ((horaF - horaI) * 2) + 1;
		} else if (minI == 30 && minF == 00) {// 3
			franjasDiarias = ((horaF - horaI) * 2) - 1;
		}
		for (int i = 0; i <= franjasDiarias; i++) {
			Franja f = new Franja();
			if (minI == 00 && i % 2 == 0) {// a en puntos minI ==0
				int hora = horaI + (i / 2);
				f.setHora("" + hora + ":00");
			} else if (minI == 00 && i % 2 != 0) {// y medias minI ==0
				int hora = horaI + (i / 2);
				f.setHora("" + hora + ":30");
			} else if (minI == 30 && i % 2 == 0) {// a y medias minI ==30
				int hora = horaI + (i / 2);
				f.setHora("" + hora + ":30");
			} else if (minI == 30 && i % 2 != 0) {// en puntos minI ==30
				int hora = horaI + ((i / 2) + 1);
				f.setHora("" + hora + ":00");
			}
			f.setFecha(fecha2sem.getTime());
			f.setZona(zona);
			franjas2sem.add(f);
		}
		return franjas2sem;
	}

	// =================HISTORICO=================

	private static String RUTA = "src/main/resources/static/historicos/";

	public static String leerArchivo(String tipo) {
		File file = new File(RUTA + tipo + ".txt");
		String doc = "";
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);

			String linea = "";
			while (linea != null) {
				linea = br.readLine();
				if (linea != null) {
					doc += linea;
				}
			}
			fr.close();
		} catch (Exception e) {
			e.getMessage();
		}
		return doc;
	}

	public static void historicoReserva(Reserva reserva) {
		try {
			File fileR = new File(RUTA + "reservas.txt");

			// $R[[idZona][#fecha],[#franja],[#numFranjas],[#idVecino]]
			String agenda = reserva.getTiene().getId() + "," + reserva.getFecha() + "," + reserva.getInicio() + ","
					+ reserva.getnBloques() + "," + reserva.getHace().getId() + "|";

			String docR = leerArchivo("reservas");
			docR += agenda;

			FileWriter fwr = new FileWriter(fileR);

			BufferedWriter bwr = new BufferedWriter(fwr);

			bwr.write(docR);

			bwr.flush();

			fwr.close();
		} catch (IOException e) {
			e.getMessage();
		}

	}

	public static void historicoVecino(Vecino vecino, ArrayList<Reserva> reservas) {
		try {
			File fileV = new File(RUTA + "vecinos.txt");
			File fileR = new File(RUTA + "reservas.txt");

			LocalDate fechaNow = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uu");
			String fechaBaja = fechaNow.format(formatter);

			// $V[[#idEdificio],[#idVecino],[#fechaBaja]]
			String vecinito = vecino.getVive().getId() + "," + vecino.getId() + "," + fechaBaja + "|";

			// $R[[idZona][#fecha],[#franja],[#numFranjas],[#idVecino]]
			ArrayList<String> agenda = new ArrayList<String>();
			for (int i = 0; i < reservas.size(); i++) {
				agenda.add(reservas.get(i).getTiene().getId() + "," + reservas.get(i).getFecha() + ","
						+ reservas.get(i).getInicio() + "," + reservas.get(i).getnBloques() + ","
						+ reservas.get(i).getHace().getId() + "|");
			}

			String docV = leerArchivo("vecinos");
			docV += vecinito;

			String docR = leerArchivo("reservas");
			for (int i = 0; i < agenda.size(); i++) {
				docR += agenda.get(i);
			}
			FileWriter fwz = new FileWriter(fileV);
			FileWriter fwr = new FileWriter(fileR);

			BufferedWriter bwz = new BufferedWriter(fwz);
			BufferedWriter bwr = new BufferedWriter(fwr);

			bwz.write(docV);
			bwr.write(docR);

			bwz.flush();
			bwr.flush();

			fwz.close();
			fwr.close();
		} catch (IOException e) {
			e.getMessage();
		}

	}

	public static void historicoZonaComun(ZonaComun zona, ArrayList<Reserva> reservas) {
		try {
			File fileZ = new File(RUTA + "zonas.txt");
			File fileR = new File(RUTA + "reservas.txt");

			LocalDate fechaNow = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uu");
			String fechaBaja = fechaNow.format(formatter);

			// $Z[#idUrba],[#idZona],[#nombreZona],[#horario],[#fechaBaja]
			String zonaComun = zona.getCorresponde().getId() + "," + zona.getId() + "," + zona.getNombre() + ","
					+ zona.getHorario() + "," + fechaBaja + "|";

			// $R[[idZona][#fecha],[#franja],[#numFranjas],[#idVecino]]
			ArrayList<String> agenda = new ArrayList<String>();
			for (int i = 0; i < reservas.size(); i++) {
				agenda.add(reservas.get(i).getTiene().getId() + "," + reservas.get(i).getFecha() + ","
						+ reservas.get(i).getnBloques() + "," + reservas.get(i).getHace().getId() + "|");
			}

			String docZ = leerArchivo("zonas");
			docZ += zonaComun;

			String docR = leerArchivo("reservas");
			for (int i = 0; i < agenda.size(); i++) {
				docR += agenda.get(i);
			}
			FileWriter fwz = new FileWriter(fileZ);
			FileWriter fwr = new FileWriter(fileR);

			BufferedWriter bwz = new BufferedWriter(fwz);
			BufferedWriter bwr = new BufferedWriter(fwr);

			bwz.write(docZ);
			bwr.write(docR);

			bwz.flush();
			bwr.flush();

			fwz.close();
			fwr.close();
		} catch (IOException e) {
			e.getMessage();
		}

	}

	public static void historicoEdificio(Edificio edificio, ArrayList<Vecino> vecinos, ArrayList<Reserva> reservas) {
		try {
			File fileE = new File(RUTA + "edificios.txt");
			File fileV = new File(RUTA + "vecinos.txt");
			File fileR = new File(RUTA + "reservas.txt");

			LocalDate fechaNow = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uu");
			String fechaBaja = fechaNow.format(formatter);

			// $E[[#idUrba],[#idEdificio],[#portal],[#pisos],[#puertasXpisos],[#fechaBaja]]
			String edificacion = edificio.getPertenece().getId() + "," + edificio.getId() + "," + edificio.getPortal()
					+ "," + edificio.getPisos() + "," + edificio.getPuertasXpiso() + "," + fechaBaja + "|";

			// $V[[#idEdificio],[#idVecino],[#fechaBaja]]
			ArrayList<String> vecindad = new ArrayList<String>();
			for (int i = 0; i < vecinos.size(); i++) {
				vecindad.add(vecinos.get(i).getVive().getId() + "," + vecinos.get(i).getId() + "," + fechaBaja + "|");
			}

			// $R[[idZona][#fecha],[#franja],[#numFranjas],[#idVecino]]
			ArrayList<String> agenda = new ArrayList<String>();
			for (int i = 0; i < reservas.size(); i++) {
				agenda.add(reservas.get(i).getTiene().getId() + "," + reservas.get(i).getFecha() + ","
						+ reservas.get(i).getnBloques() + "," + reservas.get(i).getHace().getId() + "|");
			}

			String docE = leerArchivo("edificios");
			docE += edificacion;

			String docV = leerArchivo("vecinos");
			for (int i = 0; i < vecindad.size(); i++) {
				docV += vecindad.get(i);
			}

			String docR = leerArchivo("reservas");
			for (int i = 0; i < agenda.size(); i++) {
				docR += agenda.get(i);
			}

			FileWriter fwe = new FileWriter(fileE);
			FileWriter fwv = new FileWriter(fileV);
			FileWriter fwr = new FileWriter(fileR);

			BufferedWriter bwe = new BufferedWriter(fwe);
			BufferedWriter bwv = new BufferedWriter(fwv);
			BufferedWriter bwr = new BufferedWriter(fwr);

			bwe.write(docE);
			bwv.write(docV);
			bwr.write(docR);

			bwe.flush();
			bwv.flush();
			bwr.flush();

			fwe.close();
			fwv.close();
			fwr.close();
		} catch (IOException e) {
			e.getMessage();
		}

	}

	public static void historicoUrbanizacion(Urbanizacion urba, ArrayList<Edificio> edificios,
			ArrayList<Vecino> vecinos, ArrayList<ZonaComun> zonas, ArrayList<Reserva> reservas) {
		try {
			File fileU = new File(RUTA + "urbanizaciones.txt");
			File fileE = new File(RUTA + "edificios.txt");
			File fileV = new File(RUTA + "vecinos.txt");
			File fileZ = new File(RUTA + "zonas.txt");
			File fileR = new File(RUTA + "reservas.txt");

			LocalDate fechaNow = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uu");
			String fechaBaja = fechaNow.format(formatter);

			// $U[[#idUrba],[#nombreUrba],[#fechaBaja]]
			String urbanizacion = urba.getId() + "," + urba.getNombre() + "," + fechaBaja + "|";

			// $E[[#idUrba],[#idEdificio],[#portal],[#pisos],[#fechaBaja]]
			ArrayList<String> edificaciones = new ArrayList<String>();
			for (int i = 0; i < edificios.size(); i++) {
				edificaciones.add(edificios.get(i).getPertenece().getId() + "," + edificios.get(i).getId() + ","
						+ edificios.get(i).getPortal() + "," + edificios.get(i).getPisos() + ","
						+ edificios.get(i).getPuertasXpiso() + "," + fechaBaja + "|");
			}

			// $V[[#idEdificio],[#idVecino],[#fechaBaja]]
			ArrayList<String> vecindad = new ArrayList<String>();
			for (int i = 0; i < vecinos.size(); i++) {
				vecindad.add(vecinos.get(i).getVive().getId() + "," + vecinos.get(i).getId() + "," + fechaBaja + "|");
			}

			// $Z[#idUrba],[#idZona],[#nombreZona],[#horario],[#fechaBaja]
			ArrayList<String> zonasComunes = new ArrayList<String>();
			for (int i = 0; i < zonas.size(); i++) {
				zonasComunes.add(zonas.get(i).getCorresponde().getId() + "," + zonas.get(i).getId() + ","
						+ zonas.get(i).getNombre() + "," + zonas.get(i).getHorario() + "," + fechaBaja + "|");
			}
			// $R[[idZona][#fecha],[#franja],[#numFranjas],[#idVecino]]
			ArrayList<String> agenda = new ArrayList<String>();
			for (int i = 0; i < reservas.size(); i++) {
				agenda.add(reservas.get(i).getTiene().getId() + "," + reservas.get(i).getFecha() + ","
						+ reservas.get(i).getnBloques() + "," + reservas.get(i).getHace().getId() + "|");
			}

			String docU = leerArchivo("urbanizaciones");
			docU += urbanizacion;

			String docE = leerArchivo("edificios");
			for (int i = 0; i < edificaciones.size(); i++) {
				docE += edificaciones.get(i);
			}

			String docV = leerArchivo("vecinos");
			for (int i = 0; i < vecindad.size(); i++) {
				docV += vecindad.get(i);
			}

			String docZ = leerArchivo("zonas");
			for (int i = 0; i < zonasComunes.size(); i++) {
				docZ += zonasComunes.get(i);
			}

			String docR = leerArchivo("reservas");
			for (int i = 0; i < agenda.size(); i++) {
				docR += agenda.get(i);
			}

			FileWriter fwu = new FileWriter(fileU);
			FileWriter fwe = new FileWriter(fileE);
			FileWriter fwv = new FileWriter(fileV);
			FileWriter fwz = new FileWriter(fileZ);
			FileWriter fwr = new FileWriter(fileR);

			BufferedWriter bwu = new BufferedWriter(fwu);
			BufferedWriter bwe = new BufferedWriter(fwe);
			BufferedWriter bwv = new BufferedWriter(fwv);
			BufferedWriter bwz = new BufferedWriter(fwz);
			BufferedWriter bwr = new BufferedWriter(fwr);

			bwu.write(docU);
			bwe.write(docE);
			bwv.write(docV);
			bwz.write(docZ);
			bwr.write(docR);

			bwu.flush();
			bwe.flush();
			bwv.flush();
			bwz.flush();
			bwr.flush();

			fwu.close();
			fwe.close();
			fwv.close();
			fwz.close();
			fwr.close();

		} catch (IOException e) {
			e.getMessage();
		}

	}

	public static void borrarHistoricos() {
		try {
			File fileU = new File(RUTA + "urbanizaciones.txt");
			File fileE = new File(RUTA + "edificios.txt");
			File fileV = new File(RUTA + "vecinos.txt");
			File fileZ = new File(RUTA + "zonas.txt");
			File fileR = new File(RUTA + "reservas.txt");

			FileWriter fwu = new FileWriter(fileU);
			FileWriter fwe = new FileWriter(fileE);
			FileWriter fwv = new FileWriter(fileV);
			FileWriter fwz = new FileWriter(fileZ);
			FileWriter fwr = new FileWriter(fileR);

			BufferedWriter bwu = new BufferedWriter(fwu);
			BufferedWriter bwe = new BufferedWriter(fwe);
			BufferedWriter bwv = new BufferedWriter(fwv);
			BufferedWriter bwz = new BufferedWriter(fwz);
			BufferedWriter bwr = new BufferedWriter(fwr);

			bwu.write("");
			bwe.write("");
			bwv.write("");
			bwz.write("");
			bwr.write("");

			fwu.close();
			fwe.close();
			fwv.close();
			fwz.close();
			fwr.close();

		} catch (IOException e) {
			e.getMessage();
		}
	}
}