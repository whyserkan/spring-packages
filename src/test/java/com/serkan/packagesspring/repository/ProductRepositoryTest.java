package com.serkan.packagesspring.repository;

import com.serkan.packagesspring.entity.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    private static final String ID = "PKM5pGAh9yGm";
    private static final String NAME = "Axe";
    @Test
    public void test() throws Exception {
        Optional<Product> productOptional = productRepository.findById(ID);
        assertThat(productOptional.isPresent()).isEqualTo(true);
        assertThat(productOptional.get().getName()).isEqualTo(NAME);
    }
}
