package com.parroquia.App.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.parroquia.App.errores.UserNotFoundException;
import com.parroquia.App.models.entities.Role;
import com.parroquia.App.models.entities.User;
import com.parroquia.App.models.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService service;
	
	/**
	 * 
	 * @param m
	 * @return 
	 */
	@GetMapping("/personal-parroquia")
	public String listAll(Model m) {
		List<User> listaU = service.listAll();
		m.addAttribute("listaUsuarios",listaU);
		
		return "personal";
	}
	
	@GetMapping("/personal/nuevo")
	public String nuevoUser(Model m) {
		List<Role> listaR = service.listRoles();
		
		User u = new User();
		u.setEnabled(true);
		
		m.addAttribute("user", u);
		m.addAttribute("listaRoles", listaR);
		m.addAttribute("titulo", "Registrar personal");
		
		return "personal_form";
	}
	
	@PostMapping("/personal/guardar")
	public String guardarUser(User u, RedirectAttributes ra) {
		System.out.println(u);
		service.save(u);
		
		ra.addFlashAttribute("mensaje", "El usuario se ha guardado con éxito.");
		
		return "redirect:/personal-parroquia";
	}
	
	@GetMapping("/personal/editar/{id}")
	public String editarUser(@PathVariable(name = "id") Integer id, Model m, RedirectAttributes ra) {
		try {
			User u = service.get(id);
			List<Role> listaR = service.listRoles();
			
			m.addAttribute("user",u);
			m.addAttribute("titulo", "Editar personal");
			m.addAttribute("listaRoles", listaR);
			
			return "personal_form";
		}catch(UserNotFoundException ex) {
			ra.addFlashAttribute("mensaje", ex.getMessage());
			return "redirect:/personal-parroquia";
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
