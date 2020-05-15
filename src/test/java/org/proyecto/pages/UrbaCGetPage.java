package org.proyecto.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class UrbaCGetPage {
	@SuppressWarnings("unused")
	private WebDriver navegador;
	
	// =========================================
	// ===== ELEMENTOS CLAVE de la PÁGINA ======
	// =========================================
	
	@FindBy(name="nombre")
	private WebElement nombreText;
	
	@FindBy(id="botonEnvio")
	private WebElement botonDeEnviar;
	
	@FindBy(id="botonVolver")
	private WebElement botonDeVolver;
	
	// =========================================
	// =========================================
	
	public UrbaCGetPage(WebDriver navegador) {
		super();
		this.navegador = navegador;
		PageFactory.initElements(navegador, this);
	}

	public void introducirNombre(String nombre) {
		nombreText.sendKeys(nombre);
	}
	
	public void enviarFormulario() {
		botonDeEnviar.click();
	}
	public void volver() {
		botonDeVolver.click();
	}
}


