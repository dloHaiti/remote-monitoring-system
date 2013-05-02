package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import roboguice.inject.InjectResource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class RestClient {

    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);
    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss z";

    private final RestTemplate restTemplate;

    @InjectResource(R.string.dlo_server_url)
    String baseUrl;

    public RestClient() {
        restTemplate = getJsonRestTemplate();
    }

    private RestTemplate getJsonRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        converter.getObjectMapper().setDateFormat(df);

        restTemplate.getMessageConverters().add(converter);
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        return restTemplate;
    }

    public <T> T get(String uri, Class<T> responseType) {
        return restTemplate.getForObject(this.baseUrl + uri, responseType);
    }

    public boolean post(String uri, Object data) {
        String url = this.baseUrl + uri;
        try {
            restTemplate.postForObject(url, data, String.class);
        } catch (HttpClientErrorException e) {
            logger.error("Error " + e.getStatusCode() + " while posting data to url " + url, e);
            return false;
        }

        return true;
    }
}
