package com.serkan.packagesspring.service;

import com.serkan.packagesspring.dto.PackageDto;
import com.serkan.packagesspring.entity.Package;
import com.serkan.packagesspring.entity.Product;
import com.serkan.packagesspring.exception.ProductNotFoundException;
import com.serkan.packagesspring.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.serkan.packagesspring.exception.ProductNotFoundException.notFound;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductClientService productClientService;

    public Product getFromDbOrService(String id) throws ProductNotFoundException {
        Optional<Product> productOptional = productRepository
                .findById(id);

        if (productOptional.isPresent()) {
            return productOptional.get();
        } else {
            return productClientService.fetchOne(id)
                    .orElseThrow(() -> notFound(id));
        }
    }
}
