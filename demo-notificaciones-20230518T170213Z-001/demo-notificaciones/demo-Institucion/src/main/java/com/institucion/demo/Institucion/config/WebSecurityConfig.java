package com.institucion.demo.Institucion.config;

import com.institucion.demo.Institucion.services.Usuario_Servicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	Usuario_Servicio usuario_servicio;

	@Autowired
	private CustomAccessDeniedHandler customAccessDeniedHandler;

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuario_servicio)
				.passwordEncoder(NoOpPasswordEncoder.getInstance());
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/", "/inicio", "/usuario/listar", "/usuario/listar_r/{id}", "/rol/listar", "/rol/listar_p/{id}", "/rol/crear", "/usuario/registro", "/rol/modificar/{id}", "/usuario/modificar/{id}", "/usuario/eliminar/{id}", "rol//eliminar/{id}")
				.permitAll()
				.and()
				.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/logincheck")
				.usernameParameter("email")
				.passwordParameter("contraseña")
				.defaultSuccessUrl("/inicio")
				.permitAll()
				.and()
				.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login").permitAll()
				.deleteCookies("JSESSIONID")
				.and()
				.sessionManagement()
				.maximumSessions(8) // Permitir hasta 8 sesiones simultáneas
				.maxSessionsPreventsLogin(false)
				.expiredUrl("/login?expired")
				.sessionRegistry(sessionRegistry())
				.and()
				.and()
				.rememberMe()
				.tokenValiditySeconds(24 * 60 * 60) // 24 horas en segundos
				.key("secret")
				.and()
				.exceptionHandling()
				.accessDeniedPage("/login?accessDenied")
				.and()
				.csrf()
				.disable();
		http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

	}


	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}


}
