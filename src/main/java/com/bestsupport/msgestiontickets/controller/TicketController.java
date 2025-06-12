package com.bestsupport.msgestiontickets.controller;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bestsupport.msgestiontickets.dto.TicketDTO;
import com.bestsupport.msgestiontickets.dto.TicketDTOConverter;
import com.bestsupport.msgestiontickets.model.Status;
import com.bestsupport.msgestiontickets.model.Ticket;
import com.bestsupport.msgestiontickets.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Operaciones relacionadas con la gestión de Tickets")
public class TicketController {

    private final TicketService ticketService;
    private final TicketDTOConverter ticketDTOConverter;

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

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
        return ResponseEntity.ok("Ticket Service is running");
    }

    @GetMapping()
    @Operation(
            summary = "Obtener todos los tickets",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Lista de tickets encontrada",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TicketDTO.class))),
                    @ApiResponse(responseCode = "204",
                            description = "No hay tickets registrados")
            }
    )
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            ticketDTOs.add(ticketDTOConverter.convert(ticket));
        }
        return ticketDTOs.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(ticketDTOs);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener un ticket por ID",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Ticket encontrado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketDTO.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Ticket no encontrado",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Ticket ID")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        if (ticket == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ticketDTOConverter.convert(ticket));
    }

    @GetMapping("/status/{status}")
    @Operation(
            summary = "Obtener tickets por estado",
            description = "Filtra los tickets por estado (ABIERTO, EN_PROGRESO, etc.)",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Tickets encontrados",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketDTO.class))),
                    @ApiResponse(responseCode = "204",
                            description = "No hay tickets con ese estado"),
                    @ApiResponse(responseCode = "400",
                            description = "Estado inválido",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @Parameter(in = ParameterIn.PATH, name = "status", description = "Estado del Ticket")
    public ResponseEntity<List<TicketDTO>> getTicketsByStatus(@PathVariable String status) {
        try {
            Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
        List<Ticket> tickets = ticketService.getTicketsByStatus(Status.valueOf(status.toUpperCase()));
        return tickets.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tickets.stream().map(ticketDTOConverter::convert).toList());
    }

    @PostMapping()
    @Operation(
            summary = "Crear un nuevo ticket",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Ticket creado correctamente",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketDTO.class)))
            }
    )
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) {
        logger.info("Creating ticket with: {}", ticketDTO);
        Ticket ticket = ticketDTOConverter.convertToEntity(ticketDTO);
        Ticket createdTicket = ticketService.createTicket(ticket);
        return ResponseEntity.status(201).body(ticketDTOConverter.convert(createdTicket));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar un ticket existente",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Ticket actualizado",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TicketDTO.class))),
                    @ApiResponse(responseCode = "404",
                            description = "Ticket no encontrado",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @Parameter(in = ParameterIn.PATH, name = "id", description = "Ticket ID")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        Ticket updated = ticketService.updateTicket(id, ticketDTOConverter.convertToEntity(ticketDTO));
        return updated == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(ticketDTOConverter.convert(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar un ticket por ID",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Ticket eliminado correctamente"),
                    @ApiResponse(responseCode = "404",
                            description = "Ticket no encontrado",
                            content = @Content(mediaType = "application/json"))
            },
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", description = "Ticket ID")
            }
    )
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        return ticketService.deleteTicket(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
