package pl.polskieligi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication//(exclude = BatchAutoConfiguration.class)
@ImportResource("classpath:job-config.xml")
//@PropertySource(value={"file:/liga/application-prod.properties"})
public class Main extends SpringBootServletInitializer {
	@Override protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Main.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}
}
