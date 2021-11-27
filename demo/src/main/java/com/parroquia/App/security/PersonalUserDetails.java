package com.parroquia.App.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.parroquia.App.models.entities.Role;
import com.parroquia.App.models.entities.User;

public class PersonalUserDetails implements UserDetails {
	
	private User user;
	
	
	public PersonalUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Set<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> autoridad = new ArrayList<>();
		
		for(Role rol : roles) {
			autoridad.add(new SimpleGrantedAuthority(rol.getNombre()));
		}
		return autoridad;
	}

	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}

	@Override
	public boolean isEnabled() {
		
		return user.isEnabled();
	}
	
	public String getFullname() {
		
		return this.user.getNombre() + " " + this.user.getApellido();
	}

}
