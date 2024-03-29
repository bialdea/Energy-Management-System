package ro.tuc.ds2020;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Ds2020Application extends SpringBootServletInitializer {

    @Bean
    public RestTemplate restTemplate(){return new RestTemplate();}

    @Bean
    public PasswordEncoder passEncoder(){return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(Ds2020Application.class, args);
    }
}
