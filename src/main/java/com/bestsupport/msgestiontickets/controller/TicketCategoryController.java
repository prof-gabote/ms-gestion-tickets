package com.bestsupport.msgestiontickets.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bestsupport.msgestiontickets.dto.TicketDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestsupport.msgestiontickets.dto.TicketCategoryConverter;
import com.bestsupport.msgestiontickets.dto.TicketCategoryDTO;
import com.bestsupport.msgestiontickets.model.TicketCategory;
import com.bestsupport.msgestiontickets.service.TicketCategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Tickets Category", description = "Operaciones relacionadas a las categorías de cada ticket")
public class TicketCategoryController {

    private final TicketCategoryService ticketCategoryService;
    private final TicketCategoryConverter ticketCategoryConverter;

    private static final Logger logger = LoggerFactory.getLogger(TicketCategoryController.class);

    @GetMapping("/server")
    @Operation(
            summary = "Consultar el estado del servidor",
            description = "Obtiene el estado del servicio si está activo",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "El servicio está activo",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)))
            }
    )
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Ticket Category Service is running");
    }


    @GetMapping()
    @Operation(
            summary = "Obtener todas las categorías",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Lista de categorías encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TicketCategoryDTO.class))),
                    @ApiResponse(responseCode = "204",
                            description = "No hay categorías registradas")
            }
    )
    public ResponseEntity<List<TicketCategoryDTO>> getAllTicketCategories() {
        List<TicketCategory> ticketCategories = ticketCategoryService.getAllTicketCategories();

        List<TicketCategoryDTO> ticketCategoryDTOs = new ArrayList<>();

        if (ticketCategories != null && !ticketCategories.isEmpty()) {
            for (TicketCategory ticketCategory : ticketCategories) {
                TicketCategoryDTO ticketCategoryDTO = ticketCategoryConverter.convert(ticketCategory);
                if (ticketCategoryDTO != null) {
                    ticketCategoryDTOs.add(ticketCategoryDTO);
                }
            }
        }else {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ticketCategoryDTOs);
    }
}
