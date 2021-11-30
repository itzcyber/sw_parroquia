package com.parroquia.App.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.parroquia.App.models.entities.CertificadosClientes;
import com.parroquia.App.models.entities.Cliente;
import com.parroquia.App.models.entities.Role;
import com.parroquia.App.models.entities.User;
import com.parroquia.App.models.service.ClienteService;

@Controller
public class CertificadoUpload {

	@Autowired
	private ClienteService clienteService;
	
	/**
	 * Dropdown lista de certificados
	 */
	static List<String> listaCertificados = null;
	static {
	
		listaCertificados = new ArrayList<>();
		listaCertificados.add("Bautizo");
		listaCertificados.add("Comunion");
		listaCertificados.add("Confirmacion");
		listaCertificados.add("Matrimonio");
		
	}
	
	
	@GetMapping("/certificados/nuevo")
	public String nuevo(Model m, Map<String, Object> model) {
		
		List<Cliente> clientes = clienteService.listAll();
		m.addAttribute("clientes", clientes);
		m.addAttribute("cliente", new Cliente());
		m.addAttribute("clientesFiles", new ArrayList<CertificadosClientes>());
		model.put("listaCertificados", listaCertificados);
		m.addAttribute("agregar", true);
		
		return "certificados_form";
	}
	
	@GetMapping("/certificados-parroquia")
	public String clientes(Model m, String keyword, Map<String, Object> model) {
		
		//List<Cliente> clientes = clienteService.listAll();
		
		List<Cliente> clientes = null;
		
		if(keyword != null) {
			
			clientes = clienteService.findByKeyword(keyword);
			m.addAttribute("clientes", clientes);
			m.addAttribute("cliente", new Cliente());
			m.addAttribute("clientesFiles", new ArrayList<CertificadosClientes>());
			model.put("listaCertificados", listaCertificados);
			m.addAttribute("agregar", true);
		} else {
			
			clientes = clienteService.listAll();
			
			m.addAttribute("clientes", clientes);
			m.addAttribute("cliente", new Cliente());
			m.addAttribute("clientesFiles", new ArrayList<CertificadosClientes>());
			model.put("listaCertificados", listaCertificados);
			m.addAttribute("agregar", true);
		}
		
		
		return "certificados";
	}
	
	
	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Cliente c, Model m, Map<String, Object> model, RedirectAttributes ra ) {
		
		Cliente dbCliente = clienteService.save(c);
		
		if(dbCliente != null) {
			ra.addFlashAttribute("exito","El usuario se guardó correctamente");
			return "redirect:/certificados-parroquia";
		}else {
			m.addAttribute("error", "El usuario no se guardó correctamente, por favor inténtelo de nuevo.");
			m.addAttribute("cliente", c);
			model.put("listaCertificados", listaCertificados);
			return "redirect:/certificados-parroquia";
		}
		
	}
	
	
	@GetMapping("/editarCliente/{id}")
	public String editarClientes(@PathVariable Integer id, Model m, Map<String, Object> model) {
		
		Cliente c = clienteService.findById(id);
		List<CertificadosClientes> certificadosClientes = clienteService.findFilesByClienteId( id);
		List<Cliente> clientes = clienteService.listAll();
		
		m.addAttribute("clientes", clientes);
		m.addAttribute("cliente", c);
		m.addAttribute("clientesFiles", certificadosClientes);
		model.put("listaCertificados", listaCertificados);
		m.addAttribute("agregar", false);
		
		return "certificados_form";
	}
	
	@PostMapping("/actualizar")
	public String actualizar(@ModelAttribute Cliente c, Model m, Map<String, Object> model, RedirectAttributes ra) {
		
		Cliente dbCliente = clienteService.update(c);
		
		if(dbCliente != null) {
			ra.addFlashAttribute("exito","El usuario se actualizó correctamente");
			return "redirect:/certificados-parroquia";
		}else {
			m.addAttribute("error", "El usuario no se actualizó, por favor inténtelo de nuevo.");
			m.addAttribute("cliente", c);
			model.put("listaCertificados", listaCertificados);
			return "redirect:/certificados-parroquia";
		}
	}
	
	@GetMapping("/eliminarCertificado/{id}")
	public String eliminarCertificado(@PathVariable Integer id, RedirectAttributes ra) {

		clienteService.deleteFilesById(id);
		clienteService.deleteCliente(id);
		ra.addFlashAttribute("exito","El usuario se eliminó con éxito");
		
		return "redirect:/certificados-parroquia";
	}
	
	
	@GetMapping("/detalle/{id}")
	public String detalleClientes(@PathVariable Integer id, Model m) {
		
		Cliente c = clienteService.findById(id);
		List<CertificadosClientes> certificadosClientes = clienteService.findFilesByClienteId( id);
		List<Cliente> clientes = clienteService.listAll();
		
		m.addAttribute("clientes", clientes);
		m.addAttribute("cliente", c);
		m.addAttribute("clientesFiles", certificadosClientes);
		
		return "detalle-certificado";
	}
	
}
