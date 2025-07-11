package br.nom.dailton.pocresttemplateinterceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PocRestTemplateInterceptorApplication {
	private static final Logger log = LoggerFactory.getLogger(PocRestTemplateInterceptorApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext cac = SpringApplication.run(PocRestTemplateInterceptorApplication.class, args);

		TheAPIClient theAPIClient = cac.getBean(TheAPIClient.class);
		log.atInfo().log("=====\nTHE API CLIENT FIRST CALL:{}", theAPIClient.getFirstMessage());
		log.atInfo().log("=====\nTHE API CLIENT SECOND CALL:{}", theAPIClient.getFirstMessage());
		try {
			Thread.sleep(7000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		log.atInfo().log("=====\nTHE API CLIENT THIRD CALL:{}", theAPIClient.getFirstMessage());
	}

}
