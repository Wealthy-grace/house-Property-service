package com.example.propertyservice.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "property")
public class PropertyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    private Integer quantity;
    private BigDecimal rentAmount;
    private BigDecimal securityDeposit;
    private String address;

    @Column(name = "rentalcondition", nullable = false, columnDefinition = "TEXT")
    private String rentalcondition;
    private String condition;
    private String postalCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "location_type", length = 50)
    private  LocationType locationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "house_type", length = 50)
    private HouseType houseType;

    private String availableDate;

    private Integer bedrooms;


    @Column(name = "interior", columnDefinition = "TEXT")
    private String interior;

    private String surfaceArea;
    @Column(name = "image", length = 1000)
    private String image;

    @Column(name = "image2", length = 1000)
    private String image2;

    @Column(name = "image3", length = 1000)
    private String image3;

    @Column(name = "image4", length = 1000)
    private String image4;


}
