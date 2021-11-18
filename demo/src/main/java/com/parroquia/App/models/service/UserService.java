package com.parroquia.App.models.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.parroquia.App.errores.UserNotFoundException;
import com.parroquia.App.models.dao.RoleRepository;
import com.parroquia.App.models.dao.UserRepository;
import com.parroquia.App.models.entities.Role;
import com.parroquia.App.models.entities.User;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository rolRepo;
	
	@Autowired
	private PasswordEncoder paswordE;
	
	public List<User> listAll(){
		
		return (List<User>) userRepo.findAll();
	}
	
	public List<Role> listRoles(){
		
		return (List<Role>) rolRepo.findAll();
	}

	public User save(User u) {
		
		boolean actualizacionUser = (u.getId() != null);
		if (actualizacionUser) {
			User userActual = userRepo.findById(u.getId()).get();
			
			if (u.getPassword().isEmpty()) {
				u.setPassword(userActual.getPassword());
			} else {
				encodePassword(u);
			}
			
		}else {
			encodePassword(u);
		}
		
		return userRepo.save(u);
		
	}
	
	private void encodePassword(User u) {
		
		String encodedP = paswordE.encode(u.getPassword());
		u.setPassword(encodedP);
	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return userRepo.findById(id).get();
		} catch(NoSuchElementException ex) {
			throw new UserNotFoundException("No se pude encontrar ningún usuario ID: "+id);
		}
	}
	
	
	public void eliminar(Integer id) throws UserNotFoundException {
		
		Long countById = userRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("No se pude encontrar ningún usuario ID: "+id);
		}
		
		userRepo.deleteById(id);
	}
	
	public void actualizarEstadoUser(Integer u, boolean e) {
		
		userRepo.actualizacionEstado(u, e);
		
	}

}
