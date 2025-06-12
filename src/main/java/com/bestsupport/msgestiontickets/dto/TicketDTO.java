package com.bestsupport.msgestiontickets.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Entidad que representa un Ticket")
public class TicketDTO {

    @Schema(description = "ID del Ticket", example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long ticketId;

    @Schema(description = "Título del ticket", example = "Error 404")
    private String title;

    @Schema(description = "Descripción del ticket", example = "No abre página web")
    private String description;

    @Schema(description = "Estado del ticket", example = "ABIERTO")
    private String status;

    @Schema(description = "ID del cliente", example = "1")
    private Long clientId;

    @Schema(description = "ID de la categoría del ticket", example = "1")
    private Long categoryId;

    @Schema(description = "Categoría del ticket", example = "Soporte Técnico")
    private String categoryName;

    @Schema(description = "Fecha del ticket")
    private String createdAt;
}
