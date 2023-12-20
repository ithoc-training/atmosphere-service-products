package de.ithoc.atmosphere.service.products.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@Slf4j
public class PexelsConfig {

    @Value("${pexels.api.base-url}")
    private String baseUrl;

    @Value("${pexels.api.api-key}")
    private String apiKey;


    @Bean
    public Map<String, String> pexelsApiParams() {
        log.trace("Creating pexels params bean...");

        Map<String, String> params = new HashMap<>();
        params.put("per_page", String.valueOf(1));

        log.trace("Created pexels params bean.");
        return params;
    }


    @Bean
    public RestTemplate pexelsApiClient() {
        log.trace("Creating pexels api client bean...");

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new RequestHeaderInterceptor("Authorization", apiKey));

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(interceptors);

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(this.baseUrl));

        log.trace("Created pexels api client bean.");
        return restTemplate;
    }

}
