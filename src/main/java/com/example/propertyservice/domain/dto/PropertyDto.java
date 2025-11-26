package com.example.propertyservice.domain.dto;

import com.example.propertyservice.persistence.model.HouseType;
import com.example.propertyservice.persistence.model.LocationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PropertyDto {

    private Long id;

    private String title;

    private String description;

    private BigDecimal rentAmount;
    private BigDecimal securityDeposit;
    private String address;

    private String rentalcondition;
    private String condition;
    private String postalCode;

    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @Enumerated(EnumType.STRING)
    private HouseType houseType;
    private Integer quantity;
    private String availableDate;
    private Integer bedrooms;
    private String interior;
    private String surfaceArea;
    private  Boolean propertyIsRented;
    private String image;

    private String image2;

    private String image3;

    private String image4;
}
