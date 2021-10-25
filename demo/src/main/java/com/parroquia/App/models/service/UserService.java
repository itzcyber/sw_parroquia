package com.parroquia.App.models.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.parroquia.App.errores.UserNotFoundException;
import com.parroquia.App.models.dao.RoleRepository;
import com.parroquia.App.models.dao.UserRepository;
import com.parroquia.App.models.entities.Role;
import com.parroquia.App.models.entities.User;

@Service
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

	public void save(User u) {
		encodePassword(u);
		userRepo.save(u);
		
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

}
