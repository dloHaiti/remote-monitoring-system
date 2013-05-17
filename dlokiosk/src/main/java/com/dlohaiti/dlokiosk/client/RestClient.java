package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.R;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import roboguice.inject.InjectResource;

import java.text.DateFormat;

public class RestClient {

    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    private final RestTemplate restTemplate;

    @InjectResource(R.string.dlo_server_url) String baseUrl;

    @Inject
    public RestClient(KioskDate kioskDate) {
        restTemplate = getJsonRestTemplate(kioskDate.getFormat());
    }

    private RestTemplate getJsonRestTemplate(DateFormat format) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.getObjectMapper().setDateFormat(format);

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
