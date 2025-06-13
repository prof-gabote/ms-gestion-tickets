package com.bestsupport.msgestiontickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Entidad que representa un Ticket")
public class TicketCategoryDTO {

    @Schema(description = "ID del Ticket", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long categoryId;

    @Schema(description = "Nombre de la categoría", example = "Soporte Técnico")
    private String categoryName;

}
