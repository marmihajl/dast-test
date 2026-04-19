package hr.riteh.dastdemo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "DAST Demo API",
        version = "0.0.1-SNAPSHOT",
        description = "Intentionally vulnerable Spring Boot application for DAST demonstration"
))
public class DastDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DastDemoApplication.class, args);
    }
}
