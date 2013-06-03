package com.dlohaiti.dlokiosk.client;

import com.dlohaiti.dlokiosk.KioskDate;
import com.dlohaiti.dlokiosk.R;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import roboguice.inject.InjectResource;

import java.text.DateFormat;

public class RestClient {

    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    private final RestTemplate restTemplate;
    private final ConfigurationRepository config;

    @InjectResource(R.string.dlo_server_url) String baseUrl;

    @Inject
    public RestClient(ConfigurationRepository config, KioskDate kioskDate) {
        this.restTemplate = getJsonRestTemplate(kioskDate.getFormat());
        this.config = config;
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

    private MultiValueMap<String, String> requestHeaders() {
        HttpAuthentication authHeader = new HttpBasicAuthentication(
                config.get(ConfigurationKey.KIOSK_ID),
                config.get(ConfigurationKey.KIOSK_PASSWORD)
        );
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);
        return requestHeaders;
    }

    public <T> T get(String uri, Class<T> responseType) {
        return restTemplate.exchange(this.baseUrl + uri, HttpMethod.GET, new HttpEntity<Object>(requestHeaders()), responseType, null).getBody();
    }

    public boolean post(String uri, Object data) {
        String url = this.baseUrl + uri;
        try {
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<Object>(data, requestHeaders()), String.class, null);
            return true;
        } catch (HttpClientErrorException e) {
            logger.error("Error " + e.getStatusCode() + " while posting data to url " + url, e);
            return false;
        }
    }
}
