package com.egg.sp.security.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.egg.sp.entities.Users;
import com.egg.sp.enums.Provider;
import com.egg.sp.repositories.UsersRepository;
import com.egg.sp.services.UsersService;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private UsersRepository userRepository;
	
	@Autowired
	private UsersService userService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		
		CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
		
		String email = oAuth2User.getEmail();
		
		Users users = userRepository.findByEmail(email);
		
		String name = oAuth2User.getName();
		
		String lastname = oAuth2User.getlastName();
		
		if(users == null) {
			userService.createNewCustomerAfterOAuthLoginSuccess(lastname, email, name, Provider.GOOGLE);
		}else {
			userService.updateUserAfterOAuthLoginSuccess(users, name, Provider.GOOGLE);
		}
		
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
