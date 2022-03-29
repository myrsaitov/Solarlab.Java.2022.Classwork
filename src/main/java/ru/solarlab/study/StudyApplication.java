package ru.solarlab.study;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class
})
@EnableRabbit
public class StudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyApplication.class, args);
	}

}
