package com.parroquia.App.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	public static void guardarFile(String uploadDir, String fileName, MultipartFile multipartFile) throws  IOException {
		
		Path uploadP = Paths.get(uploadDir);
		
		if (!Files.exists(uploadP)) {
			Files.createDirectories(uploadP);
		}
		
		try (InputStream is = multipartFile.getInputStream()){
			Path fileP = uploadP.resolve(fileName);
			Files.copy(is, fileP, StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException ex) {
			throw new IOException("No se pudo guardar el archivo: " + fileName, ex);
		}
		
	}
	
	public static void limpiarDir(String dir) {
		
		Path dirPath = Paths.get(dir);
		
		try {
			Files.list(dirPath).forEach(file -> {
				if(!Files.isDirectory(file)) {
					try {
						Files.delete(file);
					}catch(IOException ex) {
						System.out.println("No se pudo borrar el archivo: " + file);
					}
				}
			});
		}catch (IOException ex) {
			System.out.println("No se pudo listar el directorio: " + dirPath);
		}
		
	}
}
