package com.example.propertyservice.business;


import com.example.propertyservice.business.interfaces.PropertyService;
import com.example.propertyservice.business.mapper.PropertyMapperDto;
import com.example.propertyservice.domain.dto.PropertyDto;
import com.example.propertyservice.domain.request.PropertyRequest;
import com.example.propertyservice.domain.response.PropertyResponse;
import com.example.propertyservice.exception.PropertyAlreadyExistsException;
import com.example.propertyservice.exception.PropertyNotFoundException;

import com.example.propertyservice.persistence.model.HouseType;
import com.example.propertyservice.persistence.model.LocationType;
import com.example.propertyservice.persistence.model.PropertyEntity;
import com.example.propertyservice.persistence.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;

    @Override
    public PropertyResponse createProperty(PropertyRequest propertyRequest) {
        log.info("Creating new property with title: {}", propertyRequest.getTitle());

        // Check if property already exists (you can customize this logic)
        CheckifPropertyAleadyExists(propertyRequest);

        PropertyEntity property = PropertyMapperDto.MapPropertyEntity(propertyRequest);
        propertyRepository.save(property);


        return PropertyResponse.builder()
                .message("Property created successfully")
                .propertyId(property.getId())
                .rentAmount(property.getRentAmount())
                .title(property.getTitle())
                .description(property.getDescription())
                .image(property.getImage())
                .image2(property.getImage2())
                .image3(property.getImage3())
                .image4(property.getImage4())
                .build();
    }

    @Override
    public PropertyResponse updateProperty(PropertyRequest propertyRequest, Long id) {
        log.info("Updating property with ID: {}", id);

        if (!propertyRepository.existsById(String.valueOf(id))) {
            throw new PropertyNotFoundException("Property not found with ID: " + id);
        }

        PropertyEntity property = PropertyMapperDto.MapPropertyEntity(propertyRequest);
        property.setId(id);
        propertyRepository.save(property);
        log.info("Property updated successfully with ID: {}", id);

        return PropertyResponse.builder()
                .message("Property updated successfully")
                .success(true)
                .propertyId(id)
                .build();
    }

    @Override
    public PropertyResponse deleteProperty(Long id) {
        log.info("Deleting property with ID: {}", id);

        if (!propertyRepository.existsById(String.valueOf(id))) {
            throw new PropertyNotFoundException("Property not found with ID: " + id);
        }

        propertyRepository.deleteById(String.valueOf(id));
        log.info("Property deleted successfully with ID: {}", id);

        return PropertyResponse.builder()
                .message("Property deleted successfully")
                .success(true)
                .propertyId(id)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyResponse getPropertyById(Long id) {
        log.info("Fetching property with ID: {}", id);

        PropertyEntity property = propertyRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new PropertyNotFoundException("Property not found with ID: " + id));

        PropertyDto propertyDto = PropertyMapperDto.MapoPropertyDto(property);

        return PropertyResponse.builder()
                .propertyId(id)
                .message("Property retrieved successfully")
                .description(propertyDto.getDescription())
                .rentAmount(propertyDto.getRentAmount())
                .title(propertyDto.getTitle())
                .image(propertyDto.getImage())
                .image2(propertyDto.getImage2())
                .image3(propertyDto.getImage3())
                .image4(propertyDto.getImage4())
                .success(true)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyDto> getPropertiesByLocation(String location) {
        log.info("Searching properties by location: {}", location);

        try {
            LocationType locationType = LocationType.valueOf(location.toUpperCase());
            List<PropertyEntity> properties = propertyRepository.findByLocationType(locationType);
            return properties.stream()
                    .map(PropertyMapperDto::MapoPropertyDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid location type: {}", location);
            return List.of();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyDto> getPropertiesByHouseType(String houseType) {
        log.info("Searching properties by house type: {}", houseType);

        try {
            HouseType propertyType = HouseType.valueOf(houseType);
            List<PropertyEntity> properties = propertyRepository.findByHouseType(propertyType);
            return properties.stream()
                    .map(PropertyMapperDto::MapoPropertyDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid house type: {}", houseType);
            return List.of();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyDto> getPropertiesBySurfaceArea(String surfaceArea) {
        log.info("Searching properties by surface area: {}", surfaceArea);

        List<PropertyEntity> properties = propertyRepository.findBySurfaceAreaContainingIgnoreCase(surfaceArea);
        return properties.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyDto> getPropertiesByRentaAmount(BigDecimal rentAmount) {
        log.info("Searching properties by rent amount: {}", rentAmount);

        List<PropertyEntity> properties = propertyRepository.findByRentAmountLessThanEqual(rentAmount);
        return properties.stream()
                .map(PropertyMapperDto::MapoPropertyDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PropertyDto>  getInteriorProperties(String interior) {
        log.info("Searching properties by interior: {}", interior);

        List<PropertyEntity> properties = propertyRepository.findByInteriorContainingIgnoreCase(interior);
        return properties.stream()
                .map(PropertyMapperDto::MapoPropertyDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyDto> getAllProperties() {
        log.info("Fetching all properties");

        List<PropertyEntity> properties = propertyRepository.findAll();
        return properties.stream()
                .map(PropertyMapperDto::MapoPropertyDto)
                .collect(Collectors.toList());
    }

    private PropertyEntity mapToEntity(PropertyRequest request) {
        return PropertyEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .rentAmount(request.getRentAmount())
                .securityDeposit(request.getSecurityDeposit())
                .address(request.getStreetAddress())
                .rentalcondition(request.getRentalcondition())
                .postalCode(request.getPostalCode())
                .locationType(request.getLocationType())
                .houseType(request.getPropertyType())
                .quantity(request.getQuantity())
                .availableDate(request.getAvailableDate())
                .bedrooms(request.getBedrooms())
                .interior(request.getInterior())
                .surfaceArea(request.getSurfaceArea())
                .image(request.getImage())
                .image2(request.getImage2())
                .image3(request.getImage3())
                .image4(request.getImage4())
                .build();
    }

    private void updatePropertyEntity(PropertyEntity property, PropertyRequest request) {
        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setRentAmount(request.getRentAmount());
        property.setSecurityDeposit(request.getSecurityDeposit());
        property.setAddress(request.getStreetAddress());
        property.setRentalcondition(request.getRentalcondition());
        property.setPostalCode(request.getPostalCode());
        property.setLocationType(request.getLocationType());
        property.setHouseType(request.getPropertyType());
        property.setQuantity(request.getQuantity());
        property.setAvailableDate(request.getAvailableDate());
        property.setBedrooms(request.getBedrooms());
        property.setInterior(request.getInterior());
        property.setSurfaceArea(request.getSurfaceArea());
        property.setImage(request.getImage());
        property.setImage2(request.getImage2());
        property.setImage3(request.getImage3());
        property.setImage4(request.getImage4());
    }

    private PropertyDto mapToDto(PropertyEntity property) {
        return PropertyDto.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .rentAmount(property.getRentAmount())
                .securityDeposit(property.getSecurityDeposit())
                .address(property.getAddress())
                .rentalcondition(property.getRentalcondition())
                .condition(property.getCondition())
                .postalCode(property.getPostalCode())
                .locationType(property.getLocationType())
                .houseType(property.getHouseType())
                .quantity(property.getQuantity())
                .availableDate(property.getAvailableDate())
                .bedrooms(property.getBedrooms())
                .interior(property.getInterior())
                .surfaceArea(property.getSurfaceArea())
                .image(property.getImage())
                .image2(property.getImage2())
                .image3(property.getImage3())
                .image4(property.getImage4())
                .build();
    }


    private void CheckifPropertyAleadyExists (PropertyRequest request){

        if(propertyRepository.existsByTitle(request.getTitle())){
            throw new PropertyAlreadyExistsException("Property already exists with :" + request.getTitle());
        }

        if(propertyRepository.existsByPostalCode(request.getPostalCode())){
            throw new PropertyAlreadyExistsException("Property already exists with :" + request.getPostalCode());
        }

        if(propertyRepository.existsByAddress(request.getStreetAddress())){
            throw new PropertyAlreadyExistsException("Property already exists with :" + request.getStreetAddress());
        }

        if(propertyRepository.existsByInterior(request.getInterior())){
            throw new PropertyAlreadyExistsException("Property already exists with :" + request.getInterior());
        }

        if(propertyRepository.existsBySurfaceArea(request.getSurfaceArea())){
            throw new PropertyAlreadyExistsException("Property already exists with :" + request.getSurfaceArea());
        }
    }
}
