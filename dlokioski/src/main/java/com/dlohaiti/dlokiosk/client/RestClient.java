package com.dlohaiti.dlokiosk.client;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestClient {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public RestClient(String baseUrl) {
        this.baseUrl = baseUrl;
        restTemplate = getJsonRestTemplate();
    }

    private RestTemplate getJsonRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    public <T> T get(String uri, Class<T> responseType) {
        return restTemplate.getForObject(this.baseUrl + uri, responseType);
    }
}
