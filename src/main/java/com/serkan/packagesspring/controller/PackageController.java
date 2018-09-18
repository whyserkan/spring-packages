package com.serkan.packagesspring.controller;

import com.serkan.packagesspring.dto.PackageDto;
import com.serkan.packagesspring.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController(value = "packages")
public class PackageController {
    final static String PACKAGES_PATH = "/packages";
    final static String ID_PATH = "/{id}";
    final static String CURRENCY_PATH = "/{currency}";

    @Autowired
    private PackageService packageService;

    @PostMapping(PACKAGES_PATH)
    public ResponseEntity<PackageDto> create(@RequestBody PackageDto packageEntity) throws Exception{
        PackageDto packageDto = packageService.addNew(packageService.dtoToEntity(packageEntity));
        return ResponseEntity.ok().body(packageDto);
    }

    @GetMapping(PACKAGES_PATH)
    public ResponseEntity<List<PackageDto>> fetchAll() throws Exception{
        return ResponseEntity.ok().body(packageService.fetchAll());
    }

    @GetMapping(PACKAGES_PATH + ID_PATH)
    public ResponseEntity<PackageDto> fetch(@PathVariable("id") Long id) throws Exception{
        Optional<PackageDto> packageDto = packageService.fetch(id);
        if (packageDto.isPresent()) {
            return ResponseEntity.ok().body(packageDto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(PACKAGES_PATH + ID_PATH)
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        packageService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(PACKAGES_PATH + ID_PATH + CURRENCY_PATH)
    public ResponseEntity<PackageDto> fetchWithCurrency(@PathVariable("id") Long id,
                                                        @PathVariable("currency") String currency) throws Exception {
        Optional<PackageDto> packageDto = packageService.fetchWithAnotherCurrency(id, currency);
        if (packageDto.isPresent()) {
            return ResponseEntity.ok().body(packageDto.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
