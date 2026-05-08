package es.uji.ei1027.ei102725gbgs.utils.request;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

/**
 * Entry point.
 */
@SpringBootApplication
public class Ei102725gbgsApplication {

	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(Ei102725gbgsApplication .class.getName());


	public static void main(String[] args) {
		SpringApplication.run(Ei102725gbgsApplication.class, args);
	}
}
