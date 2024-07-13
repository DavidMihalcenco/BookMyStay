package bookMyStay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

@SpringBootApplication
public class BookMyStayApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookMyStayApplication.class, args);
	}

	@Bean
	public FreeMarkerConfigurationFactoryBean getFreeMarkerConfig() {
		FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
		bean.setTemplateLoaderPath("classpath:/templates/");

		return bean;
	}
}
