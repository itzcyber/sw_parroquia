package com.parroquia.App.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
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

	@Autowired private UserService service;
	
	
	
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
	
	

	
	@GetMapping("/personal-parroquia")
	public String listFirstPage(Model m) {
		
		return listByPage(1, m, "nombre", "asc", null);
	}
	
	
	@GetMapping("/personal-parroquia/page/{pageNum}")
	public String listByPage(@PathVariable(name="pageNum") int pageNum, Model m, 
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		//System.out.println("Sort Field " + sortField);
		//System.out.println("Sort Order " + sortDir);
		
		Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		List<User> listUser = page.getContent();
		
		long inicioCount = (pageNum - 1) * UserService.USERS_PER_PAGE + 1;
		long finCount = inicioCount + UserService.USERS_PER_PAGE - 1;
		
		if(finCount > page.getTotalElements()) {
			
			finCount = page.getTotalElements();
		}
		
		String revertirSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		m.addAttribute("actualPage", pageNum);
		m.addAttribute("totalPage", page.getTotalPages());
		m.addAttribute("inicioCount", inicioCount);
		m.addAttribute("finCount", finCount);
		m.addAttribute("totalItems", page.getTotalElements());	
		m.addAttribute("listaUsuarios", listUser);
		m.addAttribute("sortField", sortField);
		m.addAttribute("sortDir", sortDir);
		m.addAttribute("revertirSortDir", revertirSortDir);
		m.addAttribute("keyword", keyword);
		
		return "personal";
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
