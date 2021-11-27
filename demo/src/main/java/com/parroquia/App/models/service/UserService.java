package com.parroquia.App.models.service;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
	
	public static final int USERS_PER_PAGE = 5;
	
	@Autowired private UserRepository userRepo;
	@Autowired private RoleRepository rolRepo;
	@Autowired private PasswordEncoder paswordE;
	
	
	
	public List<User> listAll(){
		
		return (List<User>) userRepo.findAll();
	}
	
	
	
	public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword){
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum -1, USERS_PER_PAGE, sort);
		
		if(keyword != null) {
			
			return userRepo.findAll(keyword, pageable);
		}
		
		return userRepo.findAll(pageable);
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
	
	public boolean isEmailUnique(Integer id, String email) {
		
		User userByEmail = userRepo.getUserByEmail(email);
		
		if(userByEmail == null) return true;
		
		boolean creandoNuevo = (id == null);
		
		if(creandoNuevo) {
			if(userByEmail != null) return false;
		} else {
			if(userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
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
