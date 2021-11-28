package com.parroquia.App.models.dao;


import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.parroquia.App.models.entities.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Integer>{

	@Query("SELECT c FROM Cliente c WHERE CONCAT(c.id, '', c.rut, '', c.email, '', c.nombre, '',"
			+ "c.apellido) LIKE %?1%")
	public List<Cliente> findByKeyword(@Param("keyword")  String keyword);
	
	@Query("UPDATE Cliente c SET c.token = ?2 WHERE c.id = ?1")
	@Modifying
	void saveToken(Integer id, String token);

}
