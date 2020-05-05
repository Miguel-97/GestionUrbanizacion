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
import java.util.Random;
import org.proyecto.domain.Edificio;
import org.proyecto.domain.Urbanizacion;
import org.proyecto.domain.Vecino;
import org.proyecto.domain.ZonaComun;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class helper {

	public static String generadorPassword() {
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		char[] validas = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
		StringBuilder password = new StringBuilder(10);
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			char c = validas[random.nextInt(validas.length)];
			password.append(c);
		}
		return bpe.encode(password.toString());
	}

	public static ArrayList<Character> denomPuerta(Integer puertasXpiso) {

		ArrayList<Character> letras = new ArrayList<Character>();
		for (int i = 0; i < puertasXpiso; i++) {
			letras.add((char) (65 + i));
		}
		return letras;

	}

	// =================HISTORICO=================

	public static String leerArchivo(String tipo) {
		File file = new File("../../resources/static/historicos/" + tipo + ".txt");
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

	public static void historicoZonaComun(ZonaComun zona, String reservas) {
		try {
			File fileZ = new File("/historicos/zonas.txt");
			File fileR = new File("/historicos/reservas.txt");

			LocalDate fechaNow = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uu");
			String fechaBaja = fechaNow.format(formatter);

			// $Z[#idUrba],[#idZona],[#nombreZona],[#horario],[#fechaBaja]
			String zonaComun = zona.getCorresponde().getId() + "," + zona.getId() + "," + zona.getNombre() + ","
					+ zona.getHorario() + "," + fechaBaja + "|";

			// TODO añadir reservas como clase en vez del string
			// $R[[idZona][#fecha],[#franja],[#numFranjas],[#idVecino]]
			reservas = "idZona,12/03/20,1,2,idVecino" + "|";

			String docZ = leerArchivo("zonas");
			docZ += zonaComun;
			String docR = leerArchivo("reservas");
			docR += reservas;

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

	public static void historicoEdificio(Edificio edificio, ArrayList<Vecino> vecinos, String reservas) {
		try {
			File fileE = new File("/static/historicos/edificios.txt");
			File fileV = new File("/historicos/vecinos.txt");
			File fileR = new File("/historicos/reservas.txt");

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

			// TODO añadir reservas como clase en vez del string
			// $R[[idZona][#fecha],[#franja],[#numFranjas],[#idVecino]]
			reservas = "idZona,12/03/20,1,2,idVecino" + "|";

			String docE = leerArchivo("edificios");
			docE += edificacion;

			String docV = leerArchivo("vecinos");
			for (int i = 0; i < vecindad.size(); i++) {
				docV += vecindad.get(i);
			}

			// TODO añadir reservas como clase en vez del string
			String docR = leerArchivo("reservas");
			docR += reservas;

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
			ArrayList<Vecino> vecinos, ArrayList<ZonaComun> zonas, String reservas) {
		try {
			File fileU = new File("/historicos/urbanizaciones.txt");
			File fileE = new File("/historicos/edificios.txt");
			File fileV = new File("/historicos/vecinos.txt");
			File fileZ = new File("/historicos/zonas.txt");
			File fileR = new File("/historicos/reservas.txt");

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
			// TODO añadir reservas como clase en vez del string
			// $R[[idZona][#fecha],[#franja],[#numFranjas],[#idVecino]]
			reservas = "idZona,12/03/20,1,2,idVecino" + "|";

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

			// TODO añadir reservas como clase en vez del string
			String docR = leerArchivo("reservas");
			docR += reservas;

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
	// =============FIN=HISTORICO=================
}