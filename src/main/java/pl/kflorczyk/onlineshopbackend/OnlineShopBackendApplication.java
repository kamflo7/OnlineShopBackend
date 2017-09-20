package pl.kflorczyk.onlineshopbackend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineShopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineShopBackendApplication.class, args);

		System.out.println("test ");
	}
}
