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
                .image(propertyResponse.getImage())
                .image2(propertyResponse.getImage2())
                .image3(propertyResponse.getImage3())
                .image4(propertyResponse.getImage4())
                .build();
    }

    public static  PropertyEntity MapPropertyEntity(PropertyRequest propertyDto) {
        return PropertyEntity
                .builder()
                .title(propertyDto.getTitle())
                .description(propertyDto.getDescription())
                .rentAmount(propertyDto.getRentAmount())
                .securityDeposit(propertyDto.getSecurityDeposit())
                .quantity(propertyDto.getQuantity())
                .address(propertyDto.getStreetAddress())
                .rentalcondition(propertyDto.getRentalcondition())
                .condition(propertyDto.getRentalcondition())
                .postalCode(propertyDto.getPostalCode())
                .locationType(propertyDto.getLocationType())
                .houseType(propertyDto.getPropertyType())
                .availableDate(propertyDto.getAvailableDate())
                .bedrooms(propertyDto.getBedrooms())
                .interior(propertyDto.getInterior())
                .surfaceArea(propertyDto.getSurfaceArea())
                .image(propertyDto.getImage())
                .image2(propertyDto.getImage2())
                .image3(propertyDto.getImage3())
                .image4(propertyDto.getImage4())
                .build();
    }
}
