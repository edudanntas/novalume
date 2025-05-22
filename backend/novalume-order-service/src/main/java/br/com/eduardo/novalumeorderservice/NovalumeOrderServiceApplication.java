package br.com.eduardo.novalumeorderservice;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
@EnableRabbit
@EnableFeignClients
@Slf4j
public class NovalumeOrderServiceApplication {
    private final Environment env;

    @Value("${spring.application.name}")
    private String applicationName;

    public NovalumeOrderServiceApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NovalumeOrderServiceApplication.class);
        app.setDefaultProperties(java.util.Collections.singletonMap("spring.profiles.default", "dev"));
        Environment env = app.run(args).getEnvironment();
    }

    @PostConstruct
    public void init() {
        log.info("Initializing application '{}' with profile: {}",
                applicationName,
                Arrays.toString(env.getActiveProfiles()));
    }

    @Bean
    @Profile("dev")
    public ApplicationRunner devModeRunner() {
        return args -> {
            log.info("Application running in DEVELOPMENT mode");
            log.debug("Debug settings enabled");
        };
    }

    @Bean
    @Profile("prod")
    public ApplicationRunner prodModeRunner() {
        return args -> {
            log.info("Application running in PRODUCTION mode");
        };
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> readyEventListener() {
        return event -> {
            log.info("Application is ready to process requests");
        };
    }

}
