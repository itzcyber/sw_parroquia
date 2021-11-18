package com.parroquia.App.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
	public String listAll(Model m, Map <String, Object> model) {
		List<User> listaU = service.listAll();
		m.addAttribute("listaUsuarios",listaU);
		model.put("listaCapillas", listaCapillas);
		
		return "personal";
	}
	
	
	/**
	 * Dropdown lista parroquia
	 */
	static List<String> listaCapillas = null;
	static {
	
		listaCapillas = new ArrayList<>();
		listaCapillas.add("San Sebastian");
		listaCapillas.add("Lourdes");
		listaCapillas.add("San Pablo");
		listaCapillas.add("Sagrada Familia");
		listaCapillas.add("Chacra");
		
	}
	
	@GetMapping("/personal/nuevo")
	public String nuevoUser(Model m, Map <String, Object> model) {
		List<Role> listaR = service.listRoles();
		
		User u = new User();
		u.setEnabled(true);
		
		m.addAttribute("user", u);
		model.put("listaCapillas", listaCapillas);
		m.addAttribute("listaRoles", listaR);
		m.addAttribute("titulo", "Registrar personal");
		
		return "personal_form";
	}
	
	@PostMapping("/personal/guardar")
	public String guardarUser(User u, RedirectAttributes ra, @RequestParam("image") MultipartFile mf ) throws IOException {
		
		if (!mf.isEmpty()) {
			String fileName = StringUtils.cleanPath(mf.getOriginalFilename());
			u.setFoto(fileName);
			User guardar = service.save(u);
			
			String uploadDir = "fotos-personal/" + guardar.getId();
			
			FileUploadUtil.limpiarDir(uploadDir);
			FileUploadUtil.guardarFile(uploadDir, fileName, mf);	
		} else {
			
			if(u.getFoto().isEmpty()) u.setFoto(null);
			service.save(u);
			
		}

		
		
		ra.addFlashAttribute("mensaje", "El usuario se ha guardado con éxito.");
		
		return "redirect:/personal-parroquia";
	}
	
	@GetMapping("/personal/editar/{id}")
	public String editarUser(@PathVariable(name = "id") Integer id, Model m, Map <String, Object> model, RedirectAttributes ra) {
		try {
			User u = service.get(id);
			List<Role> listaR = service.listRoles();
			
			m.addAttribute("user",u);
			model.put("listaCapillas", listaCapillas);
			m.addAttribute("titulo", "Editar personal (ID: "+id+")");
			m.addAttribute("listaRoles", listaR);
			
			return "personal_form";
		}catch(UserNotFoundException ex) {
			ra.addFlashAttribute("mensaje", ex.getMessage());
			return "redirect:/personal-parroquia";
		}	
		
	}
	 
	@GetMapping("/personal/eliminar/{id}")
	public String eliminarUser(@PathVariable(name = "id") Integer id, RedirectAttributes ra) {
		try {
			
			service.eliminar(id);
			ra.addFlashAttribute("mensaje","El usuario ha sido eliminado con éxito");
			
			//return "personal_form";
			
		}catch(UserNotFoundException ex) {
			ra.addFlashAttribute("mensaje", ex.getMessage());
			
		}	
		return "redirect:/personal-parroquia";
	}
	

	@GetMapping("/personal/{id}/enabled/{estado}")
	public String actualizarEstadoUser(@PathVariable("id") Integer id, @PathVariable("estado") boolean enabled,
			RedirectAttributes ra) {
		
		service.actualizarEstadoUser(id, enabled);
		String estado = enabled ? "activado" : "desactivado";
		String msj = "El usuario ID: " +id+ " ha sido "+estado;
		
		ra.addFlashAttribute("mensaje", msj);
		
		return "redirect:/personal-parroquia";
	}
	
	
}
