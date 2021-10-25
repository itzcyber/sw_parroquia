package com.parroquia.App.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.parroquia.App.models.entities.User;

public interface UserRepository extends CrudRepository<User, Integer>{

}
