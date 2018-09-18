package com.serkan.packagesspring.dto;

import com.serkan.packagesspring.entity.Package;
import com.serkan.packagesspring.entity.Product;
import com.serkan.packagesspring.service.ProductService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class PackageDto {
    @Autowired
    private ProductService productService;

    private Long id;
    private String description;
    private Set<String> productIds;
    private Double price;

    public static PackageDto fromEntity(Package entity) {
        PackageDto packageDto = new PackageDto();
        packageDto.setDescription(entity.getDescription());
        packageDto.setPrice(entity.getPrice());
        packageDto.setProductIds(entity.getProducts().stream().map(Product::getId).collect(Collectors.toSet()));
        packageDto.setId(entity.getId());
        return packageDto;
    }
}
