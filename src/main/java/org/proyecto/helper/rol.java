package org.proyecto.helper;

import javax.servlet.http.HttpSession;


import org.proyecto.domain.Vecino;
import org.proyecto.exception.DangerException;

public class rol {
	/**
	 * 
	 * @param rol Tres posibilidades "anon", "auth", "admin"
	 * @param s   la sesión activa
	 * @throws DangerException si el rol no coincide con el del usuario activo
	 */
	public static void isRolOK(String rol, HttpSession s) throws DangerException {
		Vecino vecino= null;

		if (s.getAttribute("vecino") != null) {
			vecino = (Vecino) s.getAttribute("vecino");
		}

		// Ya sé quién ha hecho login, y si alguien lo ha hecho
		
		if (vecino == null) { // anon
			if (rol != "anon") {
				PRG.error("Rol inadecuado");
			}
		} 
		else if(vecino !=null && vecino.getUsername().equals("administrador") && rol.equals("auth")) {// auth
			PRG.error("Rol inadecuado", "/homeAdmin");
		}
		else {
			if (vecino != null && !vecino.getUsername().equals("administrador") && rol.equals("administrador")) { // admin
				PRG.error("Rol inadecuado", "/vecino/home");
			}
		}

	}
}
