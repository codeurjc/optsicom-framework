package es.optsicom.lib.web.restcontroller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@RequestMapping("/logIn")
	public ResponseEntity<?> logIn() {
		log.info("Loging user");
		
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping("/logOut")
	public ResponseEntity<?> logOut(HttpSession session) {
		log.info("Logout user");
		
		session.invalidate();
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
