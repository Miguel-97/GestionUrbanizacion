package org.proyecto.testsSelenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.proyecto.helper.H;
import org.proyecto.pages.UrbaCGetPage;
import org.proyecto.pages.UrbaCPostPage;

public class CrearUrbanizacionTest {
private WebDriver navegador;
	
	@BeforeEach
	public void setUp() {
		navegador = H.inicializarNavegador("http://localhost:8080/urbanizacion/c");
	}
	
	@Test
	public void testCUrbaPetunia() {
		(new UrbaCGetPage(navegador)).introducirNombre("Petunia");
		(new UrbaCGetPage(navegador)).enviarFormulario();
		(new UrbaCPostPage(navegador)).assertIrAListar("http://localhost:8080/urbanizacion/r");
	}
	
	@Test
	public void testCUrbaPetuniaRepetida() {
		(new UrbaCGetPage(navegador)).introducirNombre("Petunia");
		(new UrbaCGetPage(navegador)).enviarFormulario();
		(new UrbaCPostPage(navegador)).assertIrAListar("http://localhost:8080/urbanizacion/r");
	}
	
	@Test
	public void testCUrbaVacia() {
		(new UrbaCGetPage(navegador)).introducirNombre("");
		(new UrbaCGetPage(navegador)).enviarFormulario();
		(new UrbaCPostPage(navegador)).assertIrAListar("http://localhost:8080/urbanizacion/c");
	}
	
	@AfterEach
	public void tearDown() {
		navegador.close();
	}

}
