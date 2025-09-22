package com.internalworkflow.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;

@Controller
public class HomeController {
	@GetMapping({"/", "/index"})
	public String index(Authentication authentication) {
		if (authentication != null && authentication.isAuthenticated()) {
			return "redirect:/app/dashboard";
		}
		return "redirect:/login";
	}
}
