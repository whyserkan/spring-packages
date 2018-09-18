package com.serkan.packagesspring.remote;

import com.serkan.packagesspring.entity.Product;
import com.serkan.packagesspring.service.ProductClientService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductRemoteServiceTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductClientService productClientService;

    @Value("${product.user}")
    private String productServiceUsername;

    @Value("${product.password}")
    private String productServicePassword;

    @Value("${products.api.url}")
    private String allProductsApiUrl;


    @Value("${product.api.url}")
    private String oneProductApiUrl;

    private static final String SUCCESS_PRO_ID = "VqKb4tyj9V6i";
    private static final String SUCCESS_PRO_NAME = "Shield";

    @Test
    public void testIfEndPointIsReachable() throws Exception {
        ResponseEntity<Product[]> productsResponse = restTemplate
                .withBasicAuth(productServiceUsername, productServicePassword)
                .getForEntity(allProductsApiUrl, Product[].class);

        assertThat(productsResponse.getBody().length).isGreaterThan(0);
        assertThat(productsResponse.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void testIfProductsEndPointIsReachable() throws Exception {
        ResponseEntity<Product> productsResponse = restTemplate
                .withBasicAuth(productServiceUsername, productServicePassword)
                .getForEntity(String.format(oneProductApiUrl, SUCCESS_PRO_ID), Product.class);

        assertThat(productsResponse.getBody().getName()).isEqualTo(SUCCESS_PRO_NAME);
        assertThat(productsResponse.getStatusCode().is2xxSuccessful()).isTrue();
    }

    @Test
    public void testIfCanbeReachedOverClientService() {
        ResponseEntity<List<Product>> products = productClientService.fetchAllProducts();
        assertThat(products.getBody().size()).isGreaterThan(0);
        assertThat(products.getStatusCode().is2xxSuccessful()).isTrue();
    }
}
