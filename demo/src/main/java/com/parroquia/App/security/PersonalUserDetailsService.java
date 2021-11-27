package com.parroquia.App.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.parroquia.App.models.dao.UserRepository;
import com.parroquia.App.models.entities.User;

public class PersonalUserDetailsService implements UserDetailsService {

	@Autowired private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepo.getUserByEmail(email);
		
		if(user != null) {
			
			return new PersonalUserDetails(user);
		}
		
		throw new UsernameNotFoundException("No se pudo encontrar al usuario con el E-mail: " + email);
	}

}
