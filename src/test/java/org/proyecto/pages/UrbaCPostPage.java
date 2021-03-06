package org.proyecto.pages;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UrbaCPostPage {

	private WebDriver navegador;

	public UrbaCPostPage(WebDriver navegador) {
		super();
		this.navegador = navegador;
	}

	public void assertIrAListar(String estado, String url) {
		String obtengo = navegador.getCurrentUrl();
		if (estado.equals("true")) {
			assertTrue(obtengo.equals(url), "Espero " + url + " y obtengo " + obtengo);
		} else {
			assertFalse(obtengo.equals(url), "Espero " + url + " y obtengo " + obtengo);
		}

	}

	public void assertBannerContiene(String mensaje) {
		String obtengo = navegador.findElement(By.id("banner")).getText();
		assertTrue(obtengo.contains(mensaje), "Espero " + mensaje + " y obtengo " + obtengo);
	}

}
