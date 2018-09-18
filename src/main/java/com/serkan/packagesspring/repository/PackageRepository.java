package com.serkan.packagesspring.repository;

import com.serkan.packagesspring.entity.Package;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends CrudRepository<Package, Long>{
}
