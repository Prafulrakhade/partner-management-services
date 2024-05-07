package io.mosip.pms.policy.test.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class TestSecurityConfig {

	@Bean
	public HttpFirewall defaultHttpFirewall() {
		return new DefaultHttpFirewall();
	}

	@Bean
	public WebSecurity configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring().requestMatchers(allowedEndPoints());
		webSecurity.httpFirewall(defaultHttpFirewall());
		return webSecurity;
	}

	private String[] allowedEndPoints() {
		return new String[] { "/policies/**/**","**","/swagger-ui.html" };
	}

//	@Bean
//	protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
//		httpSecurity.csrf(httpEntry -> httpEntry.disable();
//		httpSecurity.httpBasic().and().authorizeRequests().anyRequest().authenticated().and().sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
//				.authenticationEntryPoint(unauthorizedEntryPoint());
//	}

	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

	@Bean
	public UserDetailsService userDetailsService() {
		List<UserDetails> users = new ArrayList<>();
		users.add(new User("misp-user", "misp",
				Arrays.asList(new SimpleGrantedAuthority("MISP"))));
		users.add(new User("policy", "policy",
				Arrays.asList(new SimpleGrantedAuthority("POLICYMANAGER"))));
		users.add(new User("partner", "partner",
				Arrays.asList(new SimpleGrantedAuthority("PARTNER"))));

		return new InMemoryUserDetailsManager(users);
	}
}