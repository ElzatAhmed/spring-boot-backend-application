package team.peiYangCoders.PeiYangResourceManagement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import team.peiYangCoders.PeiYangResourceManagement.bean.Runner;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
@EnableScheduling
public class PeiYangResourceManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeiYangResourceManagementApplication.class, args);
	}

	@Bean
	public CorsFilter corsFilter(){
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:8001"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("origin", "Access-Control-Allow-Origin",
				"Content-Type", "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("origin", "Content-Type", "Accept",
				"Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Origin",
				"Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTION"));
		UrlBasedCorsConfigurationSource ubc = new UrlBasedCorsConfigurationSource();
		ubc.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(ubc);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
}
