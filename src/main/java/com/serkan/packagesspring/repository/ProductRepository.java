package com.serkan.packagesspring.repository;

import com.serkan.packagesspring.entity.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, String> {
}
