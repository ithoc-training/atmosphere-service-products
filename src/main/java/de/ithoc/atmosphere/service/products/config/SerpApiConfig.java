package de.ithoc.atmosphere.service.products.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class SerpApiConfig {

    @Value("${serp.api.base-url}")
    private String baseUrl;

    @Value("${serp.api.engine}")
    private String engine;

    @Value("${serp.api.api-key}")
    private String apiKey;


    @Bean
    public Map<String, String> serpApiParams() {
        log.trace("Creating serpApiParams bean...");

        Map<String, String> params = new HashMap<>();
        params.put("api_key", apiKey);
        params.put("engine", engine);
        params.put("ijn", String.valueOf(0)); // first page with 100 entries

        log.trace("Created serpApiParams bean.");
        return params;
    }


    @Bean
    public RestTemplate serpApiClient() {
        log.trace("Creating serpApiExternal client bean...");

        RestTemplate restTemplate = new RestTemplateBuilder().build();
        // https://serpapi.com/search.json?q=Apple&engine=google_images&ijn=0&api_key=secret_api_key
        String baseUrl = this.baseUrl + "/search.json?api_key={api_key}&engine={engine}&ijn={ijn}&q={q}";
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(baseUrl));

        log.trace("Created serpApiExternal client bean.");
        return restTemplate;
    }

}
