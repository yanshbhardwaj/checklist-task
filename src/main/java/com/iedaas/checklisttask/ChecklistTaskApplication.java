package com.iedaas.checklisttask;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChecklistTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChecklistTaskApplication.class, args);
	}

}
