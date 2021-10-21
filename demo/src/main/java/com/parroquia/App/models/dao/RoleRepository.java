package com.parroquia.App.models.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.parroquia.App.models.entities.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{

}
