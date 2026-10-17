package com.jobscape.applicationservice.dto;

import com.jobscape.applicationservice.model.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateApplicationRequest {
    @NotBlank
    @Size(max = 255)
    private String companyName;

    @NotBlank
    @Size(max = 255)
    private String jobTitle;

    @NotBlank
    @Size(max = 255)
    private String city;

    @NotBlank
    private String country;

    @NotBlank
    private String source;

    @NotBlank
    private String workMode;

    @Size(max = 2048)
    private String url;

    @NotNull
    private ApplicationStatus initialStatus;

    private LocalDate initialDate;

    @Size(max = 1000)
    private String comment;

    private boolean createReminder;

    @NotNull
    @Positive
    private Long userId;
}
