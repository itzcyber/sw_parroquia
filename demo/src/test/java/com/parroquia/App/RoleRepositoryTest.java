package com.parroquia.App;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.parroquia.App.models.dao.RoleRepository;
import com.parroquia.App.models.entities.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {

	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCrearRol() {
		
		Role admin = new Role("Administrador", "Gestiona el personal de la parroquia encargado del ingreso de los certificados");
		Role guardarAdmin = repo.save(admin);
		assertThat(guardarAdmin.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testCrearRoles() {
		
		//Role admin = new Role("Admin", "Gestiona el personal de la parroquia");
		Role secre = new Role("Secretaria", "Gestiona los certificados de los usuarios de la parroquia");
		
		repo.saveAll(List.of(secre));
		
	}
	
}
