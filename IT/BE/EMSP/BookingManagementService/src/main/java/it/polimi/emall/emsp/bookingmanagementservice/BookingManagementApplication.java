package it.polimi.emall.emsp.bookingmanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BookingManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingManagementApplication.class, args);
	}

}
