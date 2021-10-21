package com.parroquia.App;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.parroquia.App.models.dao.UserRepository;
import com.parroquia.App.models.entities.Role;
import com.parroquia.App.models.entities.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCrearUser() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User user1 = new User("cristiano@gmail.com", "Cristiano1234", "Cristiano", "Ronaldo");
		user1.addRole(roleAdmin);
		
		User guardar = repo.save(user1);
		assertThat(guardar.getId()).isGreaterThan(0); 
	}
	
	@Test
	public void testCrearUserConDosRoles() {
		
		User user3 = new User("user3@gmail.com", "user3_2021", "user3", "user3 Apellido");
		Role roleAdmin = new Role(1);
		Role roleSecre = new Role(3);
		
		user3.addRole(roleAdmin);
		user3.addRole(roleSecre);
		
		User guardar = repo.save(user3);
		assertThat(guardar.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListarTodosLosUsers() {
		Iterable<User> lista = repo.findAll();
		lista.forEach(user -> System.out.println(user));
		
	}
	
	@Test
	public void testGetUserById() {
		User u = repo.findById(1).get();
		System.out.println(u);
		assertThat(u).isNotNull();
	}
	/**
	@Test
	public void testActulizarUsuario() {
		User u = repo.findById(1).get();
		u.setEnabled(true);
		u.setEmail("user1_2002@gmail.com");
		
		repo.save(u);
	}
	
	@Test
	public void testEliminarUsuario() {
		Integer userId = 2;
		repo.deleteById(userId); 
		
	}**/
	
}
