package model.integradorsinteclados;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//Modifica @SpringBootApplication para escanear todos los paquetes
@SpringBootApplication(scanBasePackages = {
    "model.integradorsinteclados",
    "application",
    "domain",
    "infrastructure"
})
// linea para decirle dónde buscar las interfaces JPA
@EnableJpaRepositories(basePackages = "infrastructure.persistence.repository")

@EntityScan(basePackages = "infrastructure.persistence.entities")
public class IntegradorSinTecladosApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegradorSinTecladosApplication.class, args);
    }

}
