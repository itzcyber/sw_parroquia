package com.parroquia.App;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {

	@Test
	public void testEncoderPassword() {
		BCryptPasswordEncoder passwordE = new BCryptPasswordEncoder();
		String clave = "12345678";
		String encodedP = passwordE.encode(clave);
		
		System.out.println(encodedP);
		
		boolean matches = passwordE.matches(clave, encodedP);
		 assertThat(matches).isTrue();
		
	}
	
}
