package example.spring.webflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"example.spring.webflux",
})
public class SpringWebFluxApplication {
	public static void main(final String[] args) {
		SpringApplication.run(SpringWebFluxApplication.class, args);
	}
}
