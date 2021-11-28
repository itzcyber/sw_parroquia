package com.parroquia.App.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		
		return new PersonalUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder PasswordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(PasswordEncoder());
		
		return authProvider;
	}
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
		protected void configure(HttpSecurity http) throws Exception {
			
			http.authorizeRequests()
			.antMatchers("/personal/**").hasAuthority("Administrador")
			.antMatchers("/personal-parroquia/**").hasAuthority("Administrador")
			.antMatchers("/certificados-parroquia/**").hasAuthority("Secretaria")
			.antMatchers("/certificados/**").hasAuthority("Secretaria")
			.antMatchers("/editarCliente/**").hasAuthority("Secretaria")
			.antMatchers("/detalle/**").hasAuthority("Secretaria")
			.antMatchers("/clave-acceso/**").hasAuthority("Secretaria")
			.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.permitAll()
			.and().logout().permitAll()
			.and()
				.rememberMe()
					.key("AbcdefgHijKlmnOpqrsTuvwXyz_1234567890")
					.tokenValiditySeconds(7 * 24 * 60 * 60); //1 semana
		}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**", "/css/**");
	}
	
	

	
}
