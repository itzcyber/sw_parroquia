package com.parroquia.App.models.service;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.parroquia.App.models.dao.CertificadoClienteRepository;
import com.parroquia.App.models.dao.ClienteRepository;
import com.parroquia.App.models.entities.CertificadosClientes;
import com.parroquia.App.models.entities.Cliente;

@Service
@Transactional
public class ClienteService {
	
	@Autowired private ClienteRepository clienteRepo;
	@Autowired private CertificadoClienteRepository certificadoClienteRepo; 
	@Autowired private UploadPathService uPathService;
	@Autowired ServletContext context;
	
	public List<Cliente> listAll(){
		
		return (List<Cliente>) clienteRepo.findAll();
	}

	public Cliente save(Cliente c) {
		
		c.setCreateAt(new Date());
		Cliente dbCliente = clienteRepo.save(c);
		
		if (dbCliente != null && c.getFiles() != null && c.getFiles().size() > 0) {		
			for(MultipartFile file : c.getFiles()) {
	
				if(file != null && StringUtils.hasText(file.getOriginalFilename())) {
					String fileName = file.getOriginalFilename();
					String modifiedFileName = FilenameUtils.getBaseName(fileName)+"_"+System.currentTimeMillis()+"."+FilenameUtils.getExtension(fileName);
					File storeFile = uPathService.getFilePath(modifiedFileName, "archivo");
					
					if(storeFile != null) {
						try {
							FileUtils.writeByteArrayToFile(storeFile, file.getBytes());
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
					
					CertificadosClientes files = new CertificadosClientes();
					files.setFileExtension(FilenameUtils.getExtension(fileName));
					files.setFileName(fileName);
					files.setModifiedFileName(modifiedFileName);
					files.setCliente(dbCliente);
					
					certificadoClienteRepo.save(files);
				}
			}
		}
		
		return dbCliente;
	}

	public Cliente findById(Integer id) {
		
		Optional<Cliente> cliente = clienteRepo.findById(id);
		if(cliente.isPresent()) {
			return cliente.get();
		}
		
		return null;
	}

	public List<CertificadosClientes> findFilesByClienteId(Integer id) {
		
		return certificadoClienteRepo.findFilesByClienteId(id);
	}

	public Cliente update(Cliente c) {
		c.setUpdatedAt(new Date());
		Cliente dbCliente = clienteRepo.save(c);
		
		if(c != null && c.getRemovefiles() != null && c.getRemovefiles().size() > 0) {
			certificadoClienteRepo.deleteFilesByClienteIdAndImagesNames(c.getId(), c.getRemovefiles());
			
			for(String file : c.getRemovefiles()) {
				File dbFile = new File(context.getRealPath("/archivo/"+File.separator+file));
				if (dbFile.exists()) {
					dbFile.delete();
				}
			}
		}
		
		if (dbCliente != null && c.getFiles() != null && c.getFiles().size() > 0) {	
			for(MultipartFile file : c.getFiles()) {	
				if(file != null && StringUtils.hasText(file.getOriginalFilename())) {
					String fileName = file.getOriginalFilename();
					String modifiedFileName = FilenameUtils.getBaseName(fileName)+"_"+System.currentTimeMillis()+"."+FilenameUtils.getExtension(fileName);
					File storeFile = uPathService.getFilePath(modifiedFileName, "archivo");
					
					if(storeFile != null) {
						try {
							FileUtils.writeByteArrayToFile(storeFile, file.getBytes());
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
					
					CertificadosClientes files = new CertificadosClientes();
					files.setFileExtension(FilenameUtils.getExtension(fileName));
					files.setFileName(fileName);
					files.setModifiedFileName(modifiedFileName);
					files.setCliente(dbCliente);
					
					certificadoClienteRepo.save(files);
				}
			}
		}
		
		return dbCliente;
	}

	public void deleteFilesById(Integer id) {
		List<CertificadosClientes> certiClientes = certificadoClienteRepo.findFilesByClienteId(id); 
		
		if(certiClientes != null && certiClientes.size() > 0) {
			for(CertificadosClientes dbFile : certiClientes) {
				File dbStoreFile = new File(context.getRealPath("/archivo/"+File.separator+dbFile.getModifiedFileName()));
				if(dbStoreFile.exists()) {
					dbStoreFile.delete();
				}
			}
			
			certificadoClienteRepo.deleteFilesByClienteId(id);
			
		}
	}

	public void deleteCliente(Integer id) {
		
		clienteRepo.deleteById(id);
	}

	public void saveToken(Integer id, String token) {
		
		clienteRepo.saveToken(id, token);

	}
	
	public List<Cliente> findByKeyword(String keyword) {	
			
			return clienteRepo.findByKeyword(keyword);

	}
	
}
