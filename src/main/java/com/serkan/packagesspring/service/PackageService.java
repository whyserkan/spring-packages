package com.serkan.packagesspring.service;

import com.serkan.packagesspring.dto.PackageDto;
import com.serkan.packagesspring.entity.Package;
import com.serkan.packagesspring.entity.Product;
import com.serkan.packagesspring.exception.ProductNotFoundException;
import com.serkan.packagesspring.exception.RateException;
import com.serkan.packagesspring.repository.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PackageService {
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private FixerService fixerService;

    public PackageDto addNew(Package packageDto) {
        Package savedPackage = packageRepository.save(packageDto);
        return PackageDto.fromEntity(savedPackage);
    }

    public Package dtoToEntity(PackageDto dto) {
        Package p = new Package();
        p.setId(dto.getId());
        p.setDescription(dto.getDescription());
        Set<Product> products = fetchProducts(dto.getProductIds());
        p.setProducts(products);
        products.forEach(product -> product.setAPackage(p));
        p.setPrice(totalPrice(products));
        return p;
    }

    private Set<Product> fetchProducts(Set<String> productIds) {
        Set<Product> products = new HashSet<>();
        for (String id: productIds) {
            try {
                products.add(productService.getFromDbOrService(id));
            } catch (ProductNotFoundException e) {
                e.printStackTrace();
            }
        }
        return products;
    }

    private Double totalPrice(Set<Product> products) {
        return products.stream().map(Product::getUsdPrice)
                .reduce((a,b) -> a+b)
                .orElse(0d);
    }

    public List<PackageDto> fetchAll() {
        List<PackageDto> packages = new ArrayList<>();
        packageRepository.findAll()
                .forEach(p->packages.add(PackageDto.fromEntity(p)));
        return packages;
    }

    public Optional<PackageDto> fetch(Long id) {
        Optional<PackageDto> packageDto = Optional.empty();
        Optional<Package> opPackage = packageRepository.findById(id);
        if (opPackage.isPresent()) {
            packageDto = Optional.of(PackageDto.fromEntity(opPackage.get()));
        }
        return packageDto;
    }

    public Optional<PackageDto> fetchWithAnotherCurrency(Long id, String currency) throws RateException {
        Optional<PackageDto> packageDto = fetch(id);
        if (packageDto.isPresent()) {
            packageDto.get().setPrice(fixerService
                    .convert(packageDto.get()
                            .getPrice(), currency)
                    .doubleValue());
        }
        return packageDto;
    }

    public void delete(Long id) {
        packageRepository.deleteById(id);
    }
}
