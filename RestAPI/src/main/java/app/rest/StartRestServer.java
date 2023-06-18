package app.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("app.services")
@ComponentScan("app.model")
@ComponentScan("app.persistance")
@SpringBootApplication
public class StartRestServer {
    public static void main(String [] args){
        SpringApplication.run(StartRestServer.class, args);
    }
}
