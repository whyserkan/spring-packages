package com.serkan.packagesspring.controller;

import com.serkan.packagesspring.dto.PackageDto;
import com.serkan.packagesspring.entity.Package;
import com.serkan.packagesspring.entity.Product;
import com.serkan.packagesspring.repository.PackageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductCreateIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private PackageRepository packageRepository;

    @Transactional
    @Test
    public void testIfPackageCreated() throws Exception {
        ResponseEntity r = restTemplate.postForEntity(PackageController.PACKAGES_PATH, createTestPackage(), ResponseEntity.class);
        assertThat(r.getStatusCode().is2xxSuccessful()).isTrue();
        Optional<Package> savedPackage = packageRepository.findById(1L);
        assertThat(savedPackage.isPresent()).isEqualTo(true);
        assertThat(savedPackage.get().getDescription()).isEqualTo("a desc");
        assertThat(savedPackage.get().getProducts().size()).isEqualTo(2);
        assertThat(savedPackage.get().getProducts()
                .stream()
                .map(Product::getId)
                .sorted()
                .collect(Collectors.joining(", ")))
                .isEqualTo("7dgX6XzU3Wds, DXSQpv6XVeJm");
    }

    private PackageDto createTestPackage() {
        PackageDto testPackage = new PackageDto();
        testPackage.setDescription("a desc");

        Set<String> productIds = new HashSet<>();

        productIds.add("DXSQpv6XVeJm");
        productIds.add("7dgX6XzU3Wds");

        testPackage.setProductIds(productIds);
        return testPackage;
    }
}
