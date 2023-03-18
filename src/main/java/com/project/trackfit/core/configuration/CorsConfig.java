package com.project.trackfit.core.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


import java.util.Arrays;


@Configuration
public class CorsConfig {
    @Bean
    CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration=
                new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        //TODO set this to our permitted origin/domain
//        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(Arrays.asList(
                "Origins",
                "Access-Control-Allow-Origins",
                "Content-Type",
                "Accept",
                "Authorization",
                "Origin, Accept",
                "X-Requested-With",
                "Access-Control-Request-Method",
                "Access-Control-Request-Header"

        ));
        corsConfiguration.setExposedHeaders(
                Arrays.asList(
                        "Origins",
                        "Content-Type",
                        "Accept",
                        "Authorization",
                        "Access-Control-Allow-Origins",
                        "Access-Control-Allow-Origins",
                        "Access-Control-Allow-Credentials"
                )
        );
        corsConfiguration.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );
        var urlBasedCorsConfigurationSource=
                new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration(
                "/**",
                corsConfiguration
        );
        return new CorsFilter(urlBasedCorsConfigurationSource);

    }
}
