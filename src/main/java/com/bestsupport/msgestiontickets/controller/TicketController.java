package com.bestsupport.msgestiontickets.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bestsupport.msgestiontickets.dto.TicketDTO;
import com.bestsupport.msgestiontickets.dto.TicketDTOConverter;
import com.bestsupport.msgestiontickets.model.Ticket;
import com.bestsupport.msgestiontickets.service.TicketService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final TicketDTOConverter ticketDTOConverter;

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @GetMapping("/server")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Ticket Service is running");
    }

    @GetMapping()
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        List<TicketDTO> ticketDTOs = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketDTO ticketDTO = ticketDTOConverter.convert(ticket);
            ticketDTOs.add(ticketDTO);
        }

        if (ticketDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ticketDTOs);

    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        Ticket ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        TicketDTO ticketDTO = ticketDTOConverter.convert(ticket);
        return ResponseEntity.ok(ticketDTO);
    }

    @PostMapping()
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) {

        logger.info("Creating ticket with: {}", ticketDTO);

        Ticket ticket = ticketDTOConverter.convertToEntity(ticketDTO);
        Ticket createdTicket = ticketService.createTicket(ticket);
        TicketDTO createdTicketDTO = ticketDTOConverter.convert(createdTicket);
        return ResponseEntity.created(null).body(createdTicketDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        Ticket ticket = ticketDTOConverter.convertToEntity(ticketDTO);
        Ticket updatedTicket = ticketService.updateTicket(id, ticket);
        if (updatedTicket == null) {
            return ResponseEntity.notFound().build();
        }
        TicketDTO updatedTicketDTO = ticketDTOConverter.convert(updatedTicket);
        return ResponseEntity.ok(updatedTicketDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        boolean deleted = ticketService.deleteTicket(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}
