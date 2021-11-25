package com.parroquia.App.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.parroquia.App.models.entities.CertificadosClientes;
import com.parroquia.App.models.entities.Cliente;
import com.parroquia.App.models.service.ClienteService;

@Controller
public class CrearToken {

	@Autowired private ClienteService clienteService;

	
	@GetMapping("/clave-acceso/{id}")
	public String detalleClientes(@PathVariable Integer id, Model m) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		Cliente c = clienteService.findById(id);
		List<CertificadosClientes> certificadosClientes = clienteService.findFilesByClienteId( id);
		List<Cliente> clientes = clienteService.listAll();
		
		
		m.addAttribute("clientes", clientes);
		m.addAttribute("cliente", c);
		m.addAttribute("clienteCertificado", sha256(c.getRut()));
		m.addAttribute("clientesFiles", certificadosClientes);
		
		return "/clave-acceso";
	}
	
	
	public static String sha256(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		
		byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
		
		return convertByteArrayToHexString(hash);
		
	}
	
	
	private static String convertByteArrayToHexString(byte[] arrayBytes) {
		
		StringBuffer stringBuffer = new StringBuffer();
		
		for (int i = 0; i < arrayBytes.length; i++) {
			
			stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x1, 16)
                    .substring(1));
		}
		
		return stringBuffer.toString();
	}
	
	
	@GetMapping("/cliente/{id}/token/{clave}")
	public String crearClave(@PathVariable("id") Integer id, @PathVariable("clave") String token, RedirectAttributes re) {
		
		clienteService.saveToken(id, token);
		
		re.addFlashAttribute("mensaje", "La clave de acceso se ha guardado correctamente.");
		
		return "redirect:/clave-acceso/{id}";
	}
	
}
