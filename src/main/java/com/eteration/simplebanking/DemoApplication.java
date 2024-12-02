package com.eteration.simplebanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.SortedMap;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		System.out.println("Simple Bank Application is successfully running!!!");
	}


	//todo: @ControllerAdvice sınıfı eklenecek projenin bu kısmı bitince
}
