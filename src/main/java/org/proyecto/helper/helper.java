package org.proyecto.helper;

import java.util.ArrayList;
import java.util.Random;

public class helper {
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

	public static ArrayList<Character> denomPuerta(Integer puertasXpiso) {
		ArrayList<Character> letras = new ArrayList<Character>();
		for (int i = 0; i < puertasXpiso; i++) {
			letras.add((char) (65 + i));
		}
		return letras;

	}
}