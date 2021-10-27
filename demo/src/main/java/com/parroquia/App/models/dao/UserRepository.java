package com.parroquia.App.models.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.parroquia.App.models.entities.User;

public interface UserRepository extends CrudRepository<User, Integer>{

	
	public Long countById(Integer id);
	
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
	@Modifying
	public void actualizacionEstado(Integer id, boolean enabled);
	
}
