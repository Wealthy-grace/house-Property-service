package com.example.propertyservice.domain.request;

import com.example.propertyservice.persistence.model.HouseType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertRequest {

    @NotBlank(message = "Property title is required")
    @Size(min = 5, max = 200, message = "Title must be between 5 and 200 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @NotNull(message = "Property type is required")
    private HouseType propertyType;

    @NotNull(message = "Rent amount is required")
    @DecimalMin(value = "100.0", message = "Rent must be at least €100")
    @DecimalMax(value = "5000.0", message = "Rent cannot exceed €5000")
    private BigDecimal rentAmount;

    @NotNull(message = "Security deposit is required")
    @DecimalMin(value = "0.0", message = "Security deposit cannot be negative")
    private BigDecimal securityDeposit;

    @NotBlank(message = "Street address is required")
    private String streetAddress;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "\\d{4}[A-Z]{2}", message = "Invalid Dutch postal code format")
    private String postalCode;

    @NotBlank(message = "Province is required")
    private String province;



    @Min(value = 1, message = "Number of bedrooms must be at least 1")
    @Max(value = 10, message = "Number of bedrooms cannot exceed 10")
    private Integer bedrooms;
}
