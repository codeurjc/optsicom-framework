package es.optsicom.lib.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class WebApp {

	public static void main(String[] args) {
		SpringApplication.run(WebApp.class, args);
	}
	
	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
	 
	    return new EmbeddedServletContainerCustomizer() {
	        @Override
	        public void customize(ConfigurableEmbeddedServletContainer container) {
	            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/spa2/404.html");
	            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/spa2/500.html");
	            container.addErrorPages(error404Page, error500Page);
	        }
	    };
	}

}
