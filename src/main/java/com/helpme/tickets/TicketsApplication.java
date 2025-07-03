package com.helpme.tickets;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TicketsApplication {

	public static void main(String[] args) {
		Dotenv dotenv = null;
		try {
			dotenv = Dotenv.load();
		} catch (Exception e) {
			System.out.println(".env file not found, assuming environment variables are set externally.");
		}

		// If .env is loaded, use it; otherwise, rely on system environment variables
		if (dotenv != null) {
			System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
			System.setProperty("PORT", dotenv.get("PORT"));
			System.setProperty("ENV", dotenv.get("ENV"));
			System.setProperty("POSTGRES_USER", dotenv.get("POSTGRES_USER"));
			System.setProperty("POSTGRES_PASSWORD", dotenv.get("POSTGRES_PASSWORD"));
			System.setProperty("POSTGRES_DB", dotenv.get("POSTGRES_DB"));
			System.setProperty("HOST", dotenv.get("HOST"));
			System.setProperty("RABBITMQ_HOST", dotenv.get("RABBITMQ_HOST"));
			System.setProperty("RABBITMQ_NOTIFICATION_QUEUE", dotenv.get("RABBITMQ_NOTIFICATION_QUEUE"));
			System.setProperty("CORS_ALLOWED_ORIGINS", dotenv.get("CORS_ALLOWED_ORIGINS"));
			System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
		}

		String jdbcUrl = "jdbc:postgresql://" + System.getenv("HOST") + "/" + System.getenv("POSTGRES_DB");
		System.out.println("JDBC URL: " + jdbcUrl);
		SpringApplication.run(TicketsApplication.class, args);
	}

}
