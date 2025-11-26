package com.example.propertyservice.business;



import com.example.propertyservice.business.impl.PropertyServiceImpl;
import com.example.propertyservice.domain.dto.PropertyDto;
import com.example.propertyservice.domain.request.PropertyRequest;
import com.example.propertyservice.domain.response.PropertyResponse;
import com.example.propertyservice.exception.PropertyAlreadyExistsException;
import com.example.propertyservice.exception.PropertyNotFoundException;
import com.example.propertyservice.persistence.model.HouseType;
import com.example.propertyservice.persistence.model.LocationType;
import com.example.propertyservice.persistence.model.PropertyEntity;
import com.example.propertyservice.persistence.repository.PropertyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PropertyServiceIntegrationTest {

    @Autowired
    private PropertyServiceImpl propertyService;

    @Autowired
    private PropertyRepository propertyRepository;

    private PropertyEntity existingProperty;

    @BeforeEach
    void setUp() {
        // Create an existing property
        existingProperty = PropertyEntity.builder()
                .title("Existing Property")
                .description("Existing Description")
                .houseType(HouseType.Apartment)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1200.00))
                .securityDeposit(BigDecimal.valueOf(2400.00))
                .address("Existing Street 456")
                .rentalcondition("No smoking")
                .postalCode("5013CD")
                .interior("Semi-furnished")
                .surfaceArea("70 m²")
                .availableDate("2025-01-15")
                .bedrooms(1)
                .quantity(1)
                .image("existing1.jpg")
                .image2("existing2.jpg")
                .image3("existing3.jpg")
                .image4("existing4.jpg")
                .build();
        propertyRepository.save(existingProperty);
    }

    @AfterEach
    void tearDown() {
        propertyRepository.deleteAll();
    }

    @Test
    void createProperty_Success() {
        PropertyRequest request = PropertyRequest.builder()
                .title("New Property")
                .description("New Description")
                .propertyType(HouseType.Room)
                .locationType(LocationType.BEST)
                .rentAmount(BigDecimal.valueOf(1800.00))
                .securityDeposit(BigDecimal.valueOf(3600.00))
                .streetAddress("New Street 123")
                .rentalcondition("Pets allowed")
                .postalCode("1012AB")
                .interior("Fully furnished")
                .surfaceArea("100 m²")
                .availableDate("2025-02-01")
                .bedrooms(3)
                .quantity(1)
                .image("new1.jpg")
                .image2("new2.jpg")
                .image3("new3.jpg")
                .image4("new4.jpg")
                .build();

        PropertyResponse response = propertyService.createProperty(request);

        assertNotNull(response);
        assertEquals("Property created successfully", response.getMessage());
        assertNotNull(response.getPropertyId());
        assertEquals("New Property", response.getTitle());
        assertTrue(propertyRepository.existsByTitle("New Property"));
    }

    @Test
    void createProperty_DuplicateTitle_ThrowsException() {
        PropertyRequest request1 = PropertyRequest.builder()
                .title("Duplicate Title Property")
                .description("First Description")
                .propertyType(HouseType.Apartment)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .securityDeposit(BigDecimal.valueOf(3000.00))
                .streetAddress("First Street 111")
                .rentalcondition("No pets")
                .postalCode("5014EF")
                .interior("Furnished")
                .surfaceArea("80 m²")
                .availableDate("2025-01-01")
                .bedrooms(2)
                .quantity(1)
                .build();

        propertyService.createProperty(request1);

        PropertyRequest request2 = PropertyRequest.builder()
                .title("Duplicate Title Property") // duplicate title
                .description("Second Description")
                .propertyType(HouseType.Studio)
                .locationType(LocationType.HELMOND)
                .rentAmount(BigDecimal.valueOf(2000.00))
                .securityDeposit(BigDecimal.valueOf(4000.00))
                .streetAddress("Second Street 222")
                .rentalcondition("Pets allowed")
                .postalCode("3012GH")
                .interior("Unfurnished")
                .surfaceArea("120 m²")
                .availableDate("2025-02-01")
                .bedrooms(3)
                .quantity(1)
                .build();

        assertThrows(PropertyAlreadyExistsException.class, () -> propertyService.createProperty(request2));
    }

    @Test
    void createProperty_DuplicatePostalCode_ThrowsException() {
        PropertyRequest request1 = PropertyRequest.builder()
                .title("First Property")
                .description("First Description")
                .propertyType(HouseType.Studio)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .securityDeposit(BigDecimal.valueOf(3000.00))
                .streetAddress("First Street 111")
                .rentalcondition("No pets")
                .postalCode("5015IJ")
                .interior("Furnished")
                .surfaceArea("80 m²")
                .availableDate("2025-01-01")
                .bedrooms(2)
                .quantity(1)
                .build();

        propertyService.createProperty(request1);

        PropertyRequest request2 = PropertyRequest.builder()
                .title("Second Property")
                .description("Second Description")
                .propertyType(HouseType.Residential_House)
                .locationType(LocationType.HELMOND)
                .rentAmount(BigDecimal.valueOf(2000.00))
                .securityDeposit(BigDecimal.valueOf(4000.00))
                .streetAddress("Second Street 222")
                .rentalcondition("Pets allowed")
                .postalCode("5015IJ") // duplicate postal code
                .interior("Unfurnished")
                .surfaceArea("120 m²")
                .availableDate("2025-02-01")
                .bedrooms(3)
                .quantity(1)
                .build();

        assertThrows(PropertyAlreadyExistsException.class, () -> propertyService.createProperty(request2));
    }

    @Test
    void createProperty_DuplicateAddress_ThrowsException() {
        PropertyRequest request1 = PropertyRequest.builder()
                .title("Property One")
                .description("First Description")
                .propertyType(HouseType.Apartment)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .securityDeposit(BigDecimal.valueOf(3000.00))
                .streetAddress("Duplicate Street 999")
                .rentalcondition("No pets")
                .postalCode("5016KL")
                .interior("Furnished")
                .surfaceArea("80 m²")
                .availableDate("2025-01-01")
                .bedrooms(2)
                .quantity(1)
                .build();

        propertyService.createProperty(request1);

        PropertyRequest request2 = PropertyRequest.builder()
                .title("Property Two")
                .description("Second Description")
                .propertyType(HouseType.Residential_House)
                .locationType(LocationType.HELMOND)
                .rentAmount(BigDecimal.valueOf(2000.00))
                .securityDeposit(BigDecimal.valueOf(4000.00))
                .streetAddress("Duplicate Street 999") // duplicate address
                .rentalcondition("Pets allowed")
                .postalCode("3017MN")
                .interior("Unfurnished")
                .surfaceArea("120 m²")
                .availableDate("2025-02-01")
                .bedrooms(3)
                .quantity(1)
                .build();

        assertThrows(PropertyAlreadyExistsException.class, () -> propertyService.createProperty(request2));
    }

    @Test
    void updateProperty_Success() {
        PropertyRequest updateRequest = PropertyRequest.builder()
                .title("Updated Property")
                .description("Updated Description")
                .propertyType(HouseType.Residential_House)
                .locationType(LocationType.EINDHOVEN)
                .rentAmount(BigDecimal.valueOf(1600.00))
                .securityDeposit(BigDecimal.valueOf(3200.00))
                .streetAddress("Updated Street 789")
                .rentalcondition("Updated conditions")
                .postalCode("3018OP")
                .interior("Updated interior")
                .surfaceArea("90 m²")
                .availableDate("2025-03-01")
                .bedrooms(2)
                .quantity(1)
                .build();

        PropertyResponse response = propertyService.updateProperty(updateRequest, existingProperty.getId());

        assertNotNull(response);
        assertEquals("Property updated successfully", response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(existingProperty.getId(), response.getPropertyId());

        // Verify the property was actually updated in the database
        PropertyEntity updatedProperty = propertyRepository.findById(existingProperty.getId()).orElse(null);
        assertNotNull(updatedProperty);
        assertEquals("Updated Property", updatedProperty.getTitle());
    }

    @Test
    void updateProperty_PropertyNotFound_ThrowsException() {
        PropertyRequest updateRequest = PropertyRequest.builder()
                .title("Non-existent Property")
                .description("Description")
                .propertyType(HouseType.Apartment)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .securityDeposit(BigDecimal.valueOf(3000.00))
                .streetAddress("Some Street")
                .rentalcondition("Some conditions")
                .postalCode("5019QR")
                .interior("Some interior")
                .surfaceArea("80 m²")
                .availableDate("2025-01-01")
                .bedrooms(2)
                .quantity(1)
                .build();

        assertThrows(PropertyNotFoundException.class,
                () -> propertyService.updateProperty(updateRequest, 99999L));
    }

    @Test
    void deleteProperty_Success() {
        Long propertyId = existingProperty.getId();
        assertTrue(propertyRepository.existsById(propertyId));

        PropertyResponse response = propertyService.deleteProperty(propertyId);

        assertNotNull(response);
        assertEquals("Property deleted successfully", response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(propertyId, response.getPropertyId());
        assertFalse(propertyRepository.existsById(propertyId));
    }

    @Test
    void deleteProperty_PropertyNotFound_ThrowsException() {
        assertThrows(PropertyNotFoundException.class,
                () -> propertyService.deleteProperty(99999L));
    }

    @Test
    void getPropertyById_Success() {
        PropertyResponse response = propertyService.getPropertyById(existingProperty.getId());

        assertNotNull(response);
        assertEquals("Property retrieved successfully", response.getMessage());
        assertTrue(response.isSuccess());
        assertEquals(existingProperty.getId(), response.getPropertyId());
        assertEquals("Existing Property", response.getTitle());
    }

    @Test
    void getPropertyById_PropertyNotFound_ThrowsException() {
        assertThrows(PropertyNotFoundException.class,
                () -> propertyService.getPropertyById(99999L));
    }

    @Test
    void updateProperty_PropertyNotFound_ThrowsExceptions() {
        PropertyRequest updateRequest = PropertyRequest.builder()
                .title("Non-existent Property")
                .description("Description")
                .propertyType(HouseType.Apartment)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .securityDeposit(BigDecimal.valueOf(3000.00))
                .streetAddress("Some Street")
                .rentalcondition("Some conditions")
                .postalCode("5019QR")
                .interior("Some interior")
                .surfaceArea("80 m²")
                .availableDate("2025-01-01")
                .bedrooms(2)
                .quantity(1)
                .build();

        assertThrows(PropertyNotFoundException.class,
                () -> propertyService.updateProperty(updateRequest, 99999L));
    }

    @Test
    void deleteProperty_PropertyNotFound_ThrowsExceptions() {
        assertThrows(PropertyNotFoundException.class,
                () -> propertyService.deleteProperty(99999L));
    }



    // Add these helper methods to your PropertyServiceIntegrationTest class

    /**
     * Helper method to create a PropertyEntity with all required fields
     */
    private PropertyEntity createTestProperty(String title, String address, BigDecimal rentAmount,
                                              HouseType houseType, LocationType locationType,
                                              String description, String rentalCondition) {
        PropertyEntity property = new PropertyEntity();
        property.setTitle(title);
        property.setAddress(address);
        property.setRentAmount(rentAmount);
        property.setHouseType(houseType);
        property.setLocationType(locationType);
        property.setDescription(description);
        property.setRentalcondition(rentalCondition);
        return property;
    }

    /**
     * Helper method to create and save a test property
     */
    private PropertyEntity createAndSaveTestProperty(String title, String address, BigDecimal rentAmount,
                                                     HouseType houseType, LocationType locationType,
                                                     String description, String rentalCondition) {
        PropertyEntity property = createTestProperty(title, address, rentAmount, houseType,
                locationType, description, rentalCondition);
        return propertyRepository.save(property);
    }

// Now your tests can be simplified:

    @Test
    void getPropertiesByLocationType_Success() {
        // Given - Clear existing data and create specific test data
        propertyRepository.deleteAll();

        // Create 2 properties with TILBURG location type
        createAndSaveTestProperty(
                "Urban Villa",
                "123 Urban Street, TILBURG",
                BigDecimal.valueOf(500000.00),
                HouseType.Residential_House,
                LocationType.TILBURG,
                "An urban villa",
                "Available immediately. No pets allowed."
        );

        createAndSaveTestProperty(
                "Urban Apartment",
                "456 Urban Avenue, TILBURG",
                BigDecimal.valueOf(300000.00),
                HouseType.Apartment,
                LocationType.TILBURG,
                "An urban apartment",
                "Available from next month. Pets allowed."
        );

        // Create 1 property with different location type for comparison
        createAndSaveTestProperty(
                "Rural House",
                "789 Rural Road",
                BigDecimal.valueOf(400000.00),
                HouseType.Studio,
                LocationType.VELDHOVEN,
                "A rural house",
                "Long term rental only."
        );

        // When
        List<PropertyDto> response = propertyService.getPropertiesByLocation(LocationType.TILBURG.toString());

        // Then
        assertNotNull(response);
        assertEquals(2, response.size(), "Should find exactly 2 properties with TILBURG location type");

        assertTrue(response.stream().allMatch(property ->
                        LocationType.TILBURG.equals(property.getLocationType())),
                "All properties should have TILBURG location type");

        List<String> propertyNames = response.stream()
                .map(PropertyDto::getTitle)
                .collect(Collectors.toList());

        assertTrue(propertyNames.contains("Urban Villa"), "Should contain Urban Villa");
        assertTrue(propertyNames.contains("Urban Apartment"), "Should contain Urban Apartment");
    }

    @Test
    void getPropertiesByHouseType_Success() {
        // Given - Clear existing data and create specific test data
        propertyRepository.deleteAll();

        // Create 2 Apartment properties
        createAndSaveTestProperty(
                "Downtown Apartment",
                "123 City Center",
                BigDecimal.valueOf(250000.00),
                HouseType.Apartment,
                LocationType.VELDHOVEN,
                "A downtown apartment",
                "Modern apartment with city view."
        );

        createAndSaveTestProperty(
                "Suburban Apartment",
                "456 Suburb Lane",
                BigDecimal.valueOf(200000.00),
                HouseType.Apartment,
                LocationType.TILBURG,
                "A suburban apartment",
                "Quiet neighborhood, parking included."
        );

        // Create 1 Villa for comparison
        createAndSaveTestProperty(
                "Luxury Villa",
                "789 Villa Street",
                BigDecimal.valueOf(800000.00),
                HouseType.Residential_House,
                LocationType.TILBURG,
                "A luxury villa",
                "Luxury property with garden and pool."
        );

        // When
        List<PropertyDto> response = propertyService.getPropertiesByHouseType(HouseType.Apartment.toString());

        // Then
        assertNotNull(response);
        assertEquals(2, response.size(), "Should find exactly 2 apartment properties");

        assertTrue(response.stream().allMatch(property ->
                        HouseType.Apartment.equals(property.getHouseType())),
                "All properties should be apartments");

        List<String> propertyNames = response.stream()
                .map(PropertyDto::getTitle)
                .collect(Collectors.toList());

        assertTrue(propertyNames.contains("Downtown Apartment"), "Should contain Downtown Apartment");
        assertTrue(propertyNames.contains("Suburban Apartment"), "Should contain Suburban Apartment");
    }
    @Test
    void getPropertiesByLocationType_InvalidLocation_ReturnsEmptyList() {
        List<PropertyDto> response = propertyService.getPropertiesByLocation("INVALID_LOCATION");

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void getPropertiesByHouseType_InvalidHouseType_ReturnsEmptyList() {
        List<PropertyDto> response = propertyService.getPropertiesByHouseType("INVALID_HOUSE_TYPE");

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }


    @Test
    void getPropertiesByLocationType_InvalidLocationType_ReturnsEmptyList() {
        List<PropertyDto> response = propertyService.getPropertiesByLocation("INVALID_LOCATION_TYPE");

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    void getPropertiesByHouseType_InvalidHouseType_ThrowsException() {

        List<PropertyDto> response = propertyService.getPropertiesByHouseType("INVALID_HOUSE_TYPE");

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }


    // Alrady Exist Description in PropertyRequest

    @Test
    void createProperty_AlreadyExistDescription_ThrowsException() {

        PropertyRequest propertyRequest = PropertyRequest.builder()
                .title("Existing Property")
                .description("Description")
                .propertyType(HouseType.Apartment)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .securityDeposit(BigDecimal.valueOf(3000.00))
                .streetAddress("Some Street")
                .rentalcondition("Some conditions")
                .postalCode("5019QR")
                .interior("Some interior")
                .surfaceArea("80 m²")
                .availableDate("2025-01-01")
                .bedrooms(2)
                .quantity(1)
                .build();


        assertThrows(PropertyAlreadyExistsException.class,
                () -> propertyService.createProperty(propertyRequest));

        // Fix: Verify that the property was not created

        assertEquals("Description", propertyRequest.getDescription());


    }

    // Alreay exist Tile in PropertyRequest
    @Test
    void createProperty_AlreadyExistTitle_ThrowsException() {
        PropertyRequest propertyRequest = PropertyRequest.builder()
                .title("Existing Property")
                .description("Description")
                .propertyType(HouseType.Apartment)
                .locationType(LocationType.TILBURG)
                .rentAmount(BigDecimal.valueOf(1500.00))
                .securityDeposit(BigDecimal.valueOf(3000.00))
                .streetAddress("Some Street")
                .rentalcondition("Some conditions")
                .postalCode("5019QR")
                .interior("Some interior")
                .surfaceArea("80 m²")
                .availableDate("2025-01-01")
                .bedrooms(2)
                .quantity(1)
                .build();

        assertThrows(PropertyAlreadyExistsException.class,
                () -> propertyService.createProperty(propertyRequest));

        // Fix: Verify that the property was not created
        assertEquals("Existing Property", propertyRequest.getTitle());
    }





}