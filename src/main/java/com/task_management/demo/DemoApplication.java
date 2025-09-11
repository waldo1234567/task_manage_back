package com.task_management.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		Dotenv dotenv =Dotenv.configure().directory("src/main/resources").ignoreIfMissing().load();
		System.setProperty("DB_PASSWORD", System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : dotenv.get("DB_PASSWORD"));
		System.setProperty("CLIENT_SECRET", System.getenv("CLIENT_SECRET") != null ? System.getenv("CLIENT_SECRET") : dotenv.get("CLIENT_SECRET"));
		System.setProperty("OAUTH_ISSUER", System.getenv("OAUTH_ISSUER") != null ? System.getenv("OAUTH_ISSUER") : dotenv.get("OAUTH_ISSUER"));
		SpringApplication.run(DemoApplication.class, args);
	}

}
