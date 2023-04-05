package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.practicum.stats.StatClient;

@SpringBootApplication(scanBasePackages = {"ru.practicum.stats", "ru.practicum.ewm"})
@Import({StatClient.class})
public class EwmApp {
    public static void main(String[] args) {
        SpringApplication.run(EwmApp.class, args);
    }
}
