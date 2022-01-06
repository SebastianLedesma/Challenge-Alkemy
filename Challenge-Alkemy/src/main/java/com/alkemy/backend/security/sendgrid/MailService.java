package com.alkemy.backend.security.sendgrid;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
public class MailService {

	/*@Value("${spring.sendgrid.api-key}")
	private String secretKey;*/
	
	private static final Logger logger = LoggerFactory.getLogger(MailService.class);
	
	public String sendTextEmail(String destinatario) {
		    Email from = new Email("sebastianledesma1992@gmail.com");
		    String subject = "The subject";
		    Email to = new Email(destinatario);
		    Content content = new Content("text/plain", "Te registraste en mi API hecha con Springboot.Ahora podes crear,"
		    		+ "modificar y eliminar tus personajes y películas de Disney.");
		    Mail mail = new Mail(from, subject, to, content);
		
		    SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
		    Request request = new Request();
		    try {
		      request.setMethod(Method.POST);
		      request.setEndpoint("mail/send");
		      request.setBody(mail.build());
		      Response response = sg.api(request);
		      logger.info(response.getBody());
		      return response.getBody();	     
		    } catch (IOException ex) {
		      logger.error(ex.getMessage());
		      return "";
		    }	   
	}
}
