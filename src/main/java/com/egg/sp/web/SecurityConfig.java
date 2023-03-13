package com.egg.sp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.egg.sp.security.oauth.CustomOAuth2UserService;
import com.egg.sp.security.oauth.OAuth2LoginSuccessHandler;
import com.egg.sp.services.UsersService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public UsersService usersService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usersService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.antMatchers("/oauth2/**").permitAll()
		.antMatchers("/admin/*")
		.hasRole("ADMIN")
		.antMatchers("/css/*", "/img/*", "/js/*", "/**")
		.permitAll().and()
		.formLogin()
	    .loginPage("/login")
	    .loginProcessingUrl("/logincheck")
	    .usernameParameter("email")
	    .passwordParameter("password")
	    .defaultSuccessUrl("/")
	    .permitAll().and().oauth2Login().loginPage("/login")
	    .userInfoEndpoint().userService(oAuth2UserService).and()
	    .successHandler(oAuth2LoginSuccessHandler)
	    .and()
	    .logout()
	    .logoutUrl("/logout")
		.logoutSuccessUrl("/login")
		.permitAll().and().csrf().disable();
	}
	
	@Autowired
	private CustomOAuth2UserService oAuth2UserService;
	
	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

}
