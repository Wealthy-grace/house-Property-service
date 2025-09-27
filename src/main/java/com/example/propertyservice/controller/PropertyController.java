package com.example.propertyservice.controller;

import com.example.propertyservice.business.interfaces.PropertyService;
import com.example.propertyservice.domain.dto.PropertyDto;
import com.example.propertyservice.domain.request.PropertyRequest;
import com.example.propertyservice.domain.response.PropertyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/properties")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PropertyController {

    private final PropertyService propertyService;
   // private final PropertyServiceImpl propertyServiceImpl;

    @PostMapping
    public ResponseEntity<PropertyResponse> createProperty(@Valid @RequestBody PropertyRequest propertyRequest) {
        log.info("REST request to create property: {}", propertyRequest.getTitle());

        PropertyResponse response = propertyService.createProperty(propertyRequest);
        return  ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropertyResponse> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody PropertyRequest propertyRequest) {
        log.info("REST request to update property with ID: {}", id);

        PropertyResponse response = propertyService.updateProperty(propertyRequest, id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PropertyResponse> deleteProperty(@PathVariable Long id) {
        log.info("REST request to delete property with ID: {}", id);

        PropertyResponse response = propertyService.deleteProperty(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponse> getPropertyById(@PathVariable Long id) {
        log.info("REST request to get property with ID: {}", id);

        PropertyResponse response = propertyService.getPropertyById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PropertyDto>> getAllProperties() {
        log.info("REST request to get all properties");

        List<PropertyDto> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    // Search endpoints
//    @GetMapping("/search/location")
//    public ResponseEntity<List<PropertyDto>> getPropertiesByLocation(
//            @RequestParam String location) {
//        log.info("REST request to search properties by location: {}", location);
//
//        List<PropertyDto> properties = propertyService.getPropertiesByLocation(location);
//        return ResponseEntity.ok(properties);
//    }

    // JSON body search for location
    @PostMapping("/search/location")
    public ResponseEntity<List<PropertyDto>> searchPropertiesByLocation(
            @RequestBody PropertyRequest request) {
        log.info("REST request to search properties by location: {}", request.getLocationType());

        List<PropertyDto> properties = propertyService.getPropertiesByLocation(String.valueOf(request.getLocationType()));
        return ResponseEntity.ok(properties);
    }

    // Path variable search for location
    @GetMapping("/search/location/{location}")
    public ResponseEntity<List<PropertyDto>> getPropertiesByLocationPath(
            @PathVariable String location) {
        log.info("REST request to search properties by location path: {}", location);

        List<PropertyDto> properties = propertyService.getPropertiesByLocation(location);
        return ResponseEntity.ok(properties);
    }


    @GetMapping("/search/house-type/{houseType}")
    public ResponseEntity<List<PropertyDto>> getPropertiesByHouseTypePath(
            @PathVariable String houseType) {
        log.info("REST request to search properties by house type via path: {}", houseType);

        List<PropertyDto> properties = propertyService.getPropertiesByHouseType(houseType);
        return ResponseEntity.ok(properties);
    }

    @PostMapping("/search/house-type")
    public ResponseEntity<List<PropertyDto>> searchPropertiesByHouseType(
            @RequestBody PropertyRequest request) {
        log.info("REST request to search properties by house type: {}", request.getPropertyType());

        List<PropertyDto> properties = propertyService.getPropertiesByHouseType(String.valueOf(request.getPropertyType()));
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/search/surface-area")
    public ResponseEntity<List<PropertyDto>> getPropertiesBySurfaceArea(
            @RequestParam String surfaceArea) {
        log.info("REST request to search properties by surface area: {}", surfaceArea);

        List<PropertyDto> properties = propertyService.getPropertiesBySurfaceArea(surfaceArea);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/search/rent-amount")
    public ResponseEntity<List<PropertyDto>> getPropertiesByRentAmount(
            @RequestParam BigDecimal maxRentAmount) {
        log.info("REST request to search properties by max rent amount: {}", maxRentAmount);

        List<PropertyDto> properties = propertyService.getPropertiesByRentaAmount(maxRentAmount);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/search/interior")
    public ResponseEntity<List<PropertyDto>> getPropertiesByInterior(
            @RequestParam String interior) {
        log.info("REST request to search properties by interior: {}", interior);

        List<PropertyDto> properties = propertyService. getInteriorProperties(interior);
        return ResponseEntity.ok(properties);
    }

    // Advanced search endpoint with multiple criteria
    @GetMapping("/search/advanced")
    public ResponseEntity<List<PropertyDto>> advancedSearch(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String houseType,
            @RequestParam(required = false) String surfaceArea,
            @RequestParam(required = false) BigDecimal maxRentAmount,
            @RequestParam(required = false) String interior,
            @RequestParam(required = false) Integer minBedrooms,
            @RequestParam(required = false) Integer maxBedrooms) {

        log.info("REST request for advanced property search with multiple criteria");

        List<PropertyDto> properties = propertyService.getAllProperties();

        // Apply filters based on provided parameters
        properties = properties.stream()
                .filter(property -> location == null ||
                        (property.getLocationType() != null &&
                                property.getLocationType().name().equalsIgnoreCase(location)))
                .filter(property -> houseType == null ||
                        (property.getHouseType() != null &&
                                property.getHouseType().name().equalsIgnoreCase(houseType)))
                .filter(property -> surfaceArea == null ||
                        (property.getSurfaceArea() != null &&
                                property.getSurfaceArea().toLowerCase().contains(surfaceArea.toLowerCase())))
                .filter(property -> maxRentAmount == null ||
                        (property.getRentAmount() != null &&
                                property.getRentAmount().compareTo(maxRentAmount) <= 0))
                .filter(property -> interior == null ||
                        (property.getInterior() != null &&
                                property.getInterior().toLowerCase().contains(interior.toLowerCase())))
                .filter(property -> minBedrooms == null ||
                        (property.getBedrooms() != null &&
                                property.getBedrooms() >= minBedrooms))
                .filter(property -> maxBedrooms == null ||
                        (property.getBedrooms() != null &&
                                property.getBedrooms() <= maxBedrooms))
                .toList();

        return ResponseEntity.ok(properties);
    }

    // Filter by price range
    @GetMapping("/search/price-range")
    public ResponseEntity<List<PropertyDto>> getPropertiesByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        log.info("REST request to search properties by price range: {} - {}", minPrice, maxPrice);

        List<PropertyDto> properties = propertyService.getAllProperties();
        List<PropertyDto> filteredProperties = properties.stream()
                .filter(property -> property.getRentAmount() != null &&
                        property.getRentAmount().compareTo(minPrice) >= 0 &&
                        property.getRentAmount().compareTo(maxPrice) <= 0)
                .toList();

        return ResponseEntity.ok(filteredProperties);
    }

    // Filter by bedroom count
    @GetMapping("/search/bedrooms")
    public ResponseEntity<List<PropertyDto>> getPropertiesByBedrooms(
            @RequestParam Integer bedrooms) {
        log.info("REST request to search properties by bedroom count: {}", bedrooms);

        List<PropertyDto> properties = propertyService.getAllProperties();
        List<PropertyDto> filteredProperties = properties.stream()
                .filter(property -> property.getBedrooms() != null &&
                        property.getBedrooms().equals(bedrooms))
                .toList();

        return ResponseEntity.ok(filteredProperties);
    }

    // Get available properties only
    @GetMapping("/available")
    public ResponseEntity<List<PropertyDto>> getAvailableProperties() {
        log.info("REST request to get available properties only");

        List<PropertyDto> properties = propertyService.getAllProperties();
        List<PropertyDto> availableProperties = properties.stream()
                .filter(property -> property.getQuantity() != null && property.getQuantity() > 0)
                .toList();

        return ResponseEntity.ok(availableProperties);
    }
}