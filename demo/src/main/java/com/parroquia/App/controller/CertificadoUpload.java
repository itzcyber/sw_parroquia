package com.parroquia.App.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.parroquia.App.models.service.ClienteService;

@Controller
public class CertificadoUpload {

	@Autowired
	private ClienteService clienteService;
	
	@GetMapping("/certificados-parroquia")
	public String clientes(Model m) {
		
		List<Cliente> clientes = clienteService.listAll();
		m.addAttribute("clientes", clientes);
		m.addAttribute("cliente", new Cliente());
		m.addAttribute("clientesFiles", new ArrayList<CertificadosClientes>());
		m.addAttribute("agregar", true);
		
		return "certificados";
	}
	
	@PostMapping("/guardar")
	public String guardar(@ModelAttribute Cliente c, RedirectAttributes ra, Model m) {
		
		Cliente dbCliente = clienteService.save(c);
		
		if(dbCliente != null) {
			ra.addFlashAttribute("exito","El usuario se guardó correctamente");
			return "redirect:/certificados-parroquia";
		}else {
			m.addAttribute("error", "El usuario no se guardó correctamente, por favor inténtelo de nuevo.");
			m.addAttribute("cliente", c);
			return "redirect:/certificados-parroquia";
		}
		
	}
	
	
	@GetMapping("/editarCliente/{id}")
	public String editarClientes(@PathVariable Integer id, Model m) {
		
		Cliente c = clienteService.findById(id);
		List<CertificadosClientes> certificadosClientes = clienteService.findFilesByClienteId( id);
		List<Cliente> clientes = clienteService.listAll();
		
		m.addAttribute("clientes", clientes);
		m.addAttribute("cliente", c);
		m.addAttribute("clientesFiles", certificadosClientes);
		m.addAttribute("agregar", false);
		
		return "certificados";
	}
	
	@PostMapping("/actualizar")
	public String actualizar(@ModelAttribute Cliente c, RedirectAttributes ra, Model m) {
		
		Cliente dbCliente = clienteService.update(c);
		
		if(dbCliente != null) {
			ra.addFlashAttribute("exito","El usuario se actualizó correctamente");
			return "redirect:/certificados-parroquia";
		}else {
			m.addAttribute("error", "El usuario no se actualizó, por favor inténtelo de nuevo.");
			m.addAttribute("cliente", c);
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
		
		return "/detalle-certificado";
	}
	
}
