package com.dlohaiti.dlokiosk.client;

import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

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
        MappingJackson2HttpMessageConverter jsonConverter =
                new MappingJackson2HttpMessageConverter();
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        supportedMediaTypes.add(new MediaType("application", "json"));
        jsonConverter.setSupportedMediaTypes(supportedMediaTypes);

        List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate
                .getMessageConverters();
        restTemplate.setMessageConverters(listHttpMessageConverters);
        return restTemplate;
    }

    public <T> T get(String uri, Class<T> responseType ) {
        return restTemplate.getForObject(this.baseUrl + uri, responseType);
    }
}
