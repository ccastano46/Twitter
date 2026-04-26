package eci.arem.twitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwitterApplication {

	public static void main(String[] args) {
		// Log environment variables to verify they are loaded correctly
		System.out.println("=== Backend Environment Variables ===");
		System.out.println("DB_URL: " + System.getenv("DB_URL"));
		System.out.println("DB_USERNAME: " + System.getenv("DB_USERNAME"));
		System.out.println("DB_PASSWORD: " + (System.getenv("DB_PASSWORD") != null ? "***CONFIGURED***" : "NOT SET"));
		System.out.println("AUTH0_DOMAIN: " + System.getenv("AUTH0_DOMAIN"));
		System.out.println("AUTH0_AUDIENCE: " + System.getenv("AUTH0_AUDIENCE"));
		System.out.println("AWS_BUCKET: " + System.getenv("AWS_BUCKET"));
		System.out.println("AWS_REGION: " + System.getenv("AWS_REGION"));
		System.out.println("=====================================");

		SpringApplication.run(TwitterApplication.class, args);
	}
}
