package com.example.lcwd.Electronic.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    private String categoryId;
    @NotBlank
    @Size(min = 4, message = "title must be of length 4:")
    private String title;
    @NotBlank(message = "Description can't be blank")
    private String description;

    private String coverImage;
}
