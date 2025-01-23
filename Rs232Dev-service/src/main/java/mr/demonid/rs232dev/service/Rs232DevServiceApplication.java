package mr.demonid.rs232dev.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Rs232DevServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(Rs232DevServiceApplication.class, args);
    }

}
