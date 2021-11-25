package com.parroquia.App.view.pdf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.parroquia.App.models.entities.Cliente;
import com.parroquia.App.models.service.ClienteService;

@Component("/clave-acceso")
public class ClavePdfView extends AbstractPdfView{

	@Autowired private ClienteService clienteService;
	
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Cliente c = (Cliente) model.get("cliente");
		//Cliente c = (Cliente) model.get(clienteService.listAll());
		
		//Cliente c = (Cliente) clienteService.listAll();
		
		
		
		PdfPTable tabla = new PdfPTable(1);
		tabla.setSpacingAfter(20);
		tabla.addCell("Datos del Usuario de la Parroquia");
		tabla.addCell(c.getRut());
		tabla.addCell(c.getNombre()+" "+c.getApellido());
		tabla.addCell(c.getEmail());
		//tabla.addCell(cc.getToken());
		
		
		//Cliente cc = (Cliente) model.get("clienteCertificado");
		PdfPTable tabla2 = new PdfPTable(1);
		tabla2.setSpacingAfter(20);
		tabla2.addCell("Clave acceso");
		tabla2.addCell(c.getToken());
		
		document.add(tabla);
		document.add(tabla2);
		
	}

}
