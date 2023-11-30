package com.team08.csci205_final_project.model.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "New Job Request")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewJobRequest {

    @Schema(description = "The category of the job", example = "Delivery", required = true)
    @NotBlank
    private String category;

    @Schema(description = "A description of the job", example = "Deliver documents to ABC Corp.", required = true)
    @NotBlank
    private String description;

    @Schema(description = "The name of the receiver of the job's output", example = "John Doe")
    @NotBlank
    private String receiverName;

    @Schema(description = "Physical address point in GeoJSON format", example = "{\"type\": \"Point\", \"coordinates\": [-74.005974, 40.712776]}")
    @NotNull
    private GeoJsonPoint receiverAddressPoint;


    @Schema(description = "The phone number of the receiver of the job's output", example = "+1234567890", required = true)
    @NotBlank
    private String receiverPhone;

    @Schema(description = "The email of the receiver of the job's output", example = "receiver@example.com", required = true)
    @NotBlank
    private String receiverEmail;
}

