package com.mohit.financeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

import io.swagger.v3.oas.annotations.info.Contact;



@SpringBootApplication
@OpenAPIDefinition(
	info=@Info(title="Finance App",
	             version="1.0",
				 description="Finance App",
				 contact=@Contact(
					name="Mohit",
					email="mohitmeenag2005@gmail.com",
					url="https://github.com/mohitkmeena"
					),
					license = @License(
					name="Apache 2.0",
					url="https://www.apache.org/licenses/LICENSE-2.0.html"
					)
				 ),
				 externalDocs = @ExternalDocumentation (
					description = "the finance app documentation",
					url = "https://github.com/mohitkmeena/finance-app"
				 )

)
public class FinanceappApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinanceappApplication.class, args);
	}

}

// "accountNumber": "20257688874",
// "accountNumber": "20257891632",