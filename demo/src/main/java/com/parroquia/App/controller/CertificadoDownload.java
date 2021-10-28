package com.parroquia.App.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CertificadoDownload {

	@Autowired ServletContext context;
	
	@GetMapping("/downloadfile/{fileName}/{modifiedFileName}")
	public void descargarCertificado(@PathVariable String fileName, @PathVariable String modifiedFileName, HttpServletResponse  response){
		
		String fullPath = context.getRealPath("/archivo/"+File.separator+modifiedFileName);
		File file = new File(fullPath);
		final int BUFFER_SIZE = 4096;
		
		if(file.exists()) {
			try {
				FileInputStream inputStream = new FileInputStream(file);
				String mimeType = context.getMimeType(fullPath);
				response.setContentType(mimeType);
				response.setHeader("Content-disposition", "attachment; filename="+fileName);
				OutputStream outputStream = response.getOutputStream();
				
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;
				
				while((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				
				inputStream.close();
				outputStream.close();
				
			}catch(Exception e) {
				
			}
		}
		
	}
	
}
