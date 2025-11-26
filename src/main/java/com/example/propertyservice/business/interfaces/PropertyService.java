package com.example.propertyservice.business.interfaces;

import com.example.propertyservice.domain.dto.PropertyDto;
import com.example.propertyservice.domain.request.PropertyRequest;
import com.example.propertyservice.domain.response.PropertyResponse;
import com.example.propertyservice.persistence.model.PropertyEntity;

import java.math.BigDecimal;
import java.util.List;

public interface PropertyService {

    PropertyResponse createProperty(PropertyRequest propertyRequest);

    PropertyResponse updateProperty(PropertyRequest propertyRequest, Long id);

    PropertyResponse updatePropertyStatus(Long propertyId, Boolean propertyIsRented);

    PropertyResponse deleteProperty(Long id);



    PropertyResponse getPropertyById(Long id);

    // search properties by location
    List<PropertyDto> getPropertiesByLocation(String location);

    // search properties by House Type
    List<PropertyDto> getPropertiesByHouseType(String houseType);

    // search properties by surface Araea
    List<PropertyDto> getPropertiesBySurfaceArea(String surfaceArea);

    // search by rentaAmount
    List<PropertyDto> getPropertiesByRentaAmount(BigDecimal rentAmount);

    List<PropertyDto> getInteriorProperties(String interior);

    List<PropertyDto> getAllProperties();
}
