package com.serkan.packagesspring.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = "aPackage")
public class Product {
    @Id
    private String id;
    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package aPackage;
    private String name;
    private Double usdPrice;
}
