package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.stats", "ru.practicum.ewm"})
public class EwmApp {
    public static void main(String[] args) {
        SpringApplication.run(EwmApp.class, args);
    }
}
