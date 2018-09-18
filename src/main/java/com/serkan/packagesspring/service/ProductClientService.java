package com.serkan.packagesspring.service;

import com.serkan.packagesspring.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProductClientService {
    @Value("${product.user}")
    private String productServiceUsername;

    @Value("${product.password}")
    private String productServicePassword;

    @Value("${products.api.url}")
    private String allProductsApiUrl;

    @Value("${product.api.url}")
    private String productsApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<List<Product>> fetchAllProducts() {
        return restTemplateWithAuth()
                .exchange(allProductsApiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {});
    }

    private RestTemplate restTemplateWithAuth() {
        restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(productServiceUsername, productServicePassword));
        return restTemplate;
    }

    private ResponseEntity<Product> callFetchService(String id) {
        return restTemplateWithAuth()
                .getForEntity(String.format(productsApiUrl, id), Product.class);
    }

    Optional<Product> fetchOne(String id) {
        ResponseEntity<Product> responseEntity = callFetchService(id);
        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Optional.empty();
        }
        return Optional.of(responseEntity.getBody());
    }
}
