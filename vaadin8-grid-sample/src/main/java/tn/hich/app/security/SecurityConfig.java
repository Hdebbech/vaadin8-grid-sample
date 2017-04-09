package tn.hich.app.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public PasswordEncoder encoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationProvider authProvider(){
		PasswordEncoder encoder = encoder();
		User user = new User();
		user.setUsername("admin");
		user.setPassword(encoder.encode("admin"));
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager(Arrays.asList(user));
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(manager);
		auth.setPasswordEncoder(encoder);
		return auth;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
		http.authorizeRequests().antMatchers("/VAADIN/**", "/PUSH/**", "/UIDL/**", "/login").permitAll();
		http.authorizeRequests().antMatchers("/**").fullyAuthenticated();

//		http.csrf().disable().exceptionHandling()
//				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
//				.accessDeniedPage("/accessDenied").and().authorizeRequests()
//				.antMatchers("/VAADIN/**", "/PUSH/**", "/UIDL/**", "/login", "/login/**", "/error/**",
//						"/accessDenied/**", "/vaadinServlet/**")
//				.permitAll().antMatchers("/authorized", "/**").fullyAuthenticated();
	
	}

}
