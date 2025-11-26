package com.example.propertyservice.business.mapper;

import com.example.propertyservice.domain.dto.PropertyDto;
import com.example.propertyservice.domain.request.PropertyRequest;
import com.example.propertyservice.domain.response.PropertyResponse;
import com.example.propertyservice.persistence.model.PropertyEntity;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyMapperDto {


    public static PropertyDto MapoPropertyDto(PropertyEntity propertyResponse) {
        return PropertyDto
                .builder()
                .id(propertyResponse.getId())
                .title(propertyResponse.getTitle())
                .description(propertyResponse.getDescription())
                .rentAmount(propertyResponse.getRentAmount())
                .securityDeposit(propertyResponse.getSecurityDeposit())
                .quantity(propertyResponse.getQuantity())
                .propertyIsRented(propertyResponse.isRented())
                .address(propertyResponse.getAddress())
                .rentalcondition(propertyResponse.getRentalcondition())
                .condition(propertyResponse.getCondition())
                .postalCode(propertyResponse.getPostalCode())
                .locationType(propertyResponse.getLocationType())
                .houseType(propertyResponse.getHouseType())
                .availableDate(propertyResponse.getAvailableDate())
                .bedrooms(propertyResponse.getBedrooms())
                .interior(propertyResponse.getInterior())
                .surfaceArea(propertyResponse.getSurfaceArea())
                .propertyIsRented(propertyResponse.isRented())
                .image(propertyResponse.getImage())
                .image2(propertyResponse.getImage2())
                .image3(propertyResponse.getImage3())
                .image4(propertyResponse.getImage4())
                .build();
    }

    public static PropertyEntity MapPropertyEntity(PropertyRequest request) {
        return PropertyEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .rentAmount(request.getRentAmount())
                .securityDeposit(request.getSecurityDeposit())
                .address(request.getStreetAddress())
                .rentalcondition(request.getRentalcondition())
                .condition(request.getCondition())
                .postalCode(request.getPostalCode())
                .locationType(request.getLocationType())
                .houseType(request.getPropertyType())
                .quantity(request.getQuantity())
                .availableDate(request.getAvailableDate())
                .bedrooms(request.getBedrooms())
                .numberOfRooms(request.getNumberOfRooms())
                .interior(request.getInterior())
                .surfaceArea(request.getSurfaceArea())
                .isRented(request.getPropertyIsRented() != null ? request.getPropertyIsRented() : false)
                .image(request.getImage())
                .image2(request.getImage2())
                .image3(request.getImage3())
                .image4(request.getImage4())
                .build();
    }
}
