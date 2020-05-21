package org.proyecto.testsSelenium;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.proyecto.helper.H;
import org.proyecto.helper.H.OPTION;
import org.proyecto.pages.UrbaCGetPage;
import org.proyecto.pages.UrbaCPostPage;

public class CrearUrbanizacionTest {
	private WebDriver navegador;

	@BeforeEach
	public void setUp() {
		navegador = H.inicializarNavegador("urbanizacion/c", OPTION.NORMAL);
	}

	@Test
	public void testCUrbaPetunia() {
		(new UrbaCGetPage(navegador)).introducirNombre("Petunia");
		(new UrbaCGetPage(navegador)).enviarFormulario();
		(new UrbaCPostPage(navegador)).assertIrAListar("true", "http://localhost:8080/urbanizacion/r");
	}

	@Test
	public void testCUrbaPetuniaRepetida() {
		(new UrbaCGetPage(navegador)).introducirNombre("Petunia");
		(new UrbaCGetPage(navegador)).enviarFormulario();
		(new UrbaCPostPage(navegador)).assertIrAListar("false", "http://localhost:8080/urbanizacion/r");
	}

	@Test
	public void testCUrbaVacia() {
		(new UrbaCGetPage(navegador)).introducirNombre("");
		(new UrbaCGetPage(navegador)).enviarFormulario();
		(new UrbaCPostPage(navegador)).assertIrAListar("false", "http://localhost:8080/urbanizacion/r");
	}

	@AfterEach
	public void tearDown() {
		navegador.close();
	}

}
