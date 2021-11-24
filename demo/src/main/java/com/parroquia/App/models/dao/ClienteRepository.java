package com.parroquia.App.models.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.parroquia.App.models.entities.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Integer>{

	
	@Query("UPDATE Cliente c SET c.token = ?2 WHERE c.id = ?1")
	@Modifying
	void saveToken(Integer id, String token);

}
