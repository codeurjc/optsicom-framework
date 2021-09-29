package es.optsicom.lib.web.restcontroller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.optsicom.lib.web.dto.JupyterInfoDTO;

@RestController
@RequestMapping("/api/jupyter")
public class JupyterController {

	private static final Logger log = LoggerFactory.getLogger(JupyterController.class);
	
	@Autowired
	private Environment env;

	@RequestMapping("/info")
	public ResponseEntity<?> getUrl() {
		log.info("==> Getting jupyter INFO");
		String url = env.getProperty("jupyter.url");
		if(url == null) {
			return ResponseEntity.noContent().build();
		} else {
			String templates = env.getProperty("jupyter.templates");
			String[] templatesArray = templates.split(",");
			return ResponseEntity.ok().body(new JupyterInfoDTO(url, templatesArray));
		}
	}
}
