package com.asher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) throws IOException, TimeoutException {
        SpringApplication.run(ProducerApplication.class, args);

    }
}
