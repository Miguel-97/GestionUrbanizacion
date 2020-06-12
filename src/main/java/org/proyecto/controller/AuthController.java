package org.proyecto.controller;

import javax.servlet.http.HttpSession;
import org.proyecto.exception.DangerException;
import org.proyecto.helper.rol;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

	// =========================================

		@GetMapping("/logout")
		public String logout(HttpSession s) throws DangerException {
			s.invalidate();
			return "redirect:/login";
		}
}
