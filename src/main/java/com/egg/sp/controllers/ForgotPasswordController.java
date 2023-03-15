package com.egg.sp.controllers;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.egg.sp.entities.Users;
import com.egg.sp.exceptions.UserNotFoundException;
import com.egg.sp.services.UsersService;
import com.egg.sp.utils.Utility;

import net.bytebuddy.utility.RandomString;

@Controller
public class ForgotPasswordController {

	@Autowired
	private UsersService usersService;

	@Autowired
	private JavaMailSender mailSender;

	@GetMapping("/forgot-password")
	public String showForgotPassword(Model model) {
		return "forgot-password-form";
	}

	@PostMapping("/forgot-password")
	public String processForgotPassword(HttpServletRequest request, Model model) {
		String email = request.getParameter("email");
		String token = RandomString.make(40);
		try {
			usersService.updateResetPasswordToken(token, email);
			String resetPasswordLink = Utility.getSiteUrl(request) + "/reset-password?token=" + token;

			sendEmail(email, resetPasswordLink);
			
			model.addAttribute("success", "Te enviamos un email para resetear tu contraseña, por favor revisa tu mail");

		} catch (UserNotFoundException ex) {
			model.addAttribute("error", ex.getMessage());
		} catch (UnsupportedEncodingException | MessagingException e) {
			model.addAttribute("error", "Error mientras enviamos el mail");
		} 

		return "forgot-password-form";
	}

	private void sendEmail(String email, String resetPasswordLink) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom("contact@arreglaloya.com", "Soporte ArreglaloYa");
		helper.setTo(email);
		
		String subject = "Aquí está el link para resetear tu contraseña";
		
		String content = "<p>Hola!</p>"
	            + "<p>Has solicitado restablecer su contraseña.</p>"
	            + "<p>Haga clic en el enlace de abajo para cambiar su contraseña:</p>"
	            + "<p><a href=\"" + resetPasswordLink + "\">Cambiar contraseña</a></p>"
	            + "<p>Ignora este correo electrónico si recuerdas tu contraseña, "
	            + "o no has hecho la solicitud.</p>";
		
		helper.setSubject(subject);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);

	}
	
	@GetMapping("/reset-password")
	public String showResetPasswordForm(@Param(value = "token")String token, Model model) {
		Users user = usersService.get(token);
		
		if(user == null) {
			model.addAttribute("title", "Resetear password");
			model.addAttribute("message", "Token inválido");
			return "message";
		}
		
		model.addAttribute("token", token);
		return "reset-password-form";
	}
	
	@PostMapping("/reset-password")
	public String processResetPassword(HttpServletRequest request, Model model) {
		String token = request.getParameter("token");
		
		String password = request.getParameter("password");
		
		Users user = usersService.get(token);
		
		if(user == null) {
			model.addAttribute("message", "Token inválido");
		}else {
			 usersService.updatePassword(user, password);
			 model.addAttribute("message", "Has cambiado tu contraseña con exito!");		
		}
		 return "message";
	}
	
}
