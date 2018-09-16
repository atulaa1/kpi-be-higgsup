package com.higgsup.kpi;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class KpiApplication extends SpringBootServletInitializer {

    private static Logger logger = LogManager.getLogger(KpiApplication.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(KpiApplication.class);
    }

    public static void main(String[] args) {
        logger.info("Starting Spring Boot application..");
        SpringApplication.run(KpiApplication.class, args);
    }
}
