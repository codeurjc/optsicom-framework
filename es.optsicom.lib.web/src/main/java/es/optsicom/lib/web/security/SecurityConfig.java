package es.optsicom.lib.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// API Authenticated URLS
		configureUrlAuthorization(http);

		// Disable CSRF protection (it is difficult to implement with ng2)
		http.csrf().disable();

		// Use Http Basic Authentication
		http.httpBasic();

		// Poup form login disabled
		http.formLogin().disable();

		// Do not redirect when logout
		http.logout().logoutSuccessHandler((rq, rs, a) -> {
		});
	}

	private void configureUrlAuthorization(HttpSecurity http) throws Exception {
		// Autenticated URL
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/**").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/**").hasAnyRole("ADMIN");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/**").hasAnyRole("ADMIN");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		// Enable default password encoder (mandatory since Spring Security 5 to avoid
		// storing passwords in plain text)
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

		// Obtain user and secret
		String user = env.getProperty("optsicom.api.user");
		String secret = env.getProperty("optsicom.api.secret");

		auth.inMemoryAuthentication().withUser(user).password(encoder.encode(secret)).roles("ADMIN");
	}
}
