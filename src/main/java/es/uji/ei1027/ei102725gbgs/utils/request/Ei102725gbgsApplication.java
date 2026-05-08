package es.uji.ei1027.ei102725gbgs.utils.request;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

/**
 * Entry point.
 */
@SpringBootApplication
public class Ei102725gbgsApplication {
	/**
	 * Logger for this class, used for logging application events and debugging information.
	 */
    @SuppressWarnings("unused")
    private static final Logger LOG =
        Logger.getLogger(Ei102725gbgsApplication.class.getName());


		/**
		 * Main method to start the application.
		 * @param args command-line arguments
		 */
    public static void main(String[] args) {
        SpringApplication.run(Ei102725gbgsApplication.class, args);
    }
}
