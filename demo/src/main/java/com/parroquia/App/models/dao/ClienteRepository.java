package com.parroquia.App.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.parroquia.App.models.entities.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Integer>{

	void save(String token);

}
