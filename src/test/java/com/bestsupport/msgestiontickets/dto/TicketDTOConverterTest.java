package com.bestsupport.msgestiontickets.dto;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bestsupport.msgestiontickets.model.Status;
import com.bestsupport.msgestiontickets.model.Ticket;
import com.bestsupport.msgestiontickets.model.TicketCategory;
import com.bestsupport.msgestiontickets.repository.TicketCategoryRepository;

@ExtendWith(MockitoExtension.class)
public class TicketDTOConverterTest {

    @InjectMocks
    private TicketDTOConverter ticketDTOConverter;

    @Mock
    private TicketCategoryRepository ticketCategoryRepository;

    @Test
    void convert_ValidTicket_ShouldReturnTicketDTO() {
        // Given
        
        Random random = new Random();
        TicketCategory ticketCategory = new TicketCategory();
        ticketCategory.setId(1L);
        ticketCategory.setCategoryName("General");
        ticketCategory.setTickets(Arrays.asList(new Ticket(), new Ticket()));

        Ticket ticket = new Ticket();
        ticket.setId(random.nextLong());
        ticket.setTitle("Test Ticket");
        ticket.setDescription("This is a test ticket.");
        ticket.setStatus(Status.ABIERTO);
        ticket.setClientId(1L);
        ticket.setTicketCategory(ticketCategory);
        ticket.setCreatedAt(new java.util.Date());

        // When
        TicketDTO ticketDTO = ticketDTOConverter.convert(ticket);

        // Then
        assertTrue(ticketDTO != null);
        assertEquals(ticket.getId(), ticketDTO.getTicketId());
        assertEquals(ticket.getTitle(), ticketDTO.getTitle());
        assertEquals(ticket.getDescription(), ticketDTO.getDescription());
        assertEquals(Status.valueOf(ticketDTO.getStatus()), ticketDTO.getStatus());
        assertEquals(ticket.getClientId(), ticketDTO.getClientId());
        assertEquals(ticket.getTicketCategory().getId(), ticketDTO.getCategoryId());
        assertEquals(ticket.getTicketCategory().getCategoryName(), ticketDTO.getCategoryName());
        assertTrue(ticketDTO.getCreatedAt() != null);
    }

    @Test
    void convertToEntity_ValidTicketDTO_ShouldReturnTicket() {
        // Given
        TicketCategory ticketCategory = new TicketCategory();
        ticketCategory.setId(1L);
        ticketCategory.setCategoryName("General");
        ticketCategory.setTickets(Arrays.asList(new Ticket(), new Ticket()));

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketId(1L);
        ticketDTO.setTitle("Test Ticket");
        ticketDTO.setDescription("This is a test ticket.");
        ticketDTO.setStatus(Status.ABIERTO.toString());
        ticketDTO.setClientId(1L);
        ticketDTO.setCategoryId(ticketCategory.getId());
        ticketDTO.setCategoryName(ticketCategory.getCategoryName());
        ticketDTO.setCreatedAt(new java.util.Date().toString());

        when(ticketCategoryRepository.findById(ticketDTO.getCategoryId()))
                .thenReturn(Optional.of(ticketCategory));

        // When
        Ticket ticket = ticketDTOConverter.convertToEntity(ticketDTO);

        // Then
        assertTrue(ticket != null);
        assertEquals(ticketDTO.getTicketId(), ticket.getId());
        assertEquals(ticketDTO.getTitle(), ticket.getTitle());
        assertEquals(ticketDTO.getDescription(), ticket.getDescription());
        assertEquals(Status.valueOf(ticketDTO.getStatus()), ticket.getStatus());
        assertEquals(ticketDTO.getClientId(), ticket.getClientId());
        assertEquals(ticketDTO.getCategoryId(), ticket.getTicketCategory().getId());
        assertEquals(ticketDTO.getCategoryName(), ticket.getTicketCategory().getCategoryName());
        assertTrue(ticket.getCreatedAt() != null);
    }

}
