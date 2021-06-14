package ro.taxiApp.docs.web.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

import ro.taxiApp.docs.services.organization.RoleService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private RoleService roleService;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers(HttpMethod.OPTIONS, "/**")
			.antMatchers("/**/*.{js,html,ico}");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		List<String> allRoleNames = roleService.getAllRoleNames();
		String[] allRoleNamesAsArray = allRoleNames.toArray(new String[allRoleNames.size()]);
		
		http
			.csrf()
				.disable()
			.headers()
				.frameOptions().disable()
			.exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint)
		.and()
			.sessionManagement()
	        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    .and()
	    	.authorizeRequests()
		        .antMatchers("/rest/Auth/login").permitAll()
		        .antMatchers("/rest/Auth/register").permitAll() 
		        .antMatchers("/rest/Auth/getLoggedInUser").authenticated()
		        .antMatchers("/rest/**").hasAnyAuthority(allRoleNamesAsArray)
		        .anyRequest().authenticated()
	    .and()
	        .addFilterBefore(jwtAuthenticationFilter, SecurityContextHolderAwareRequestFilter.class);
	}
}
