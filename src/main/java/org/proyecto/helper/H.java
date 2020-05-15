package org.proyecto.helper;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class H {

	public static enum OPTION {
		MAXIMIZAR, SILENCIOSO, NORMAL
	};

	private static final String DRIVER_PATH = "src/main/resources/drivers/chromedriver.exe";
	private static final String URL_BASE = "http://localhost:8080/";

	public static WebDriver inicializarNavegador(String url, OPTION opcion) {
		System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
		WebDriver navegador;

		if (opcion.equals(OPTION.SILENCIOSO)) {
			navegador = new ChromeDriver(new ChromeOptions().addArguments("--headless"));
		} else {
			navegador = new ChromeDriver();
		}

		if (opcion == OPTION.MAXIMIZAR) {
			navegador.manage().window().maximize();
		}

		navegador.navigate().to(URL_BASE + url);

		return navegador;
	}

	public static WebDriver inicializarNavegador(String url) {
		return inicializarNavegador(url, OPTION.SILENCIOSO);
	}

}
