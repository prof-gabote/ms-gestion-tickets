package com.bestsupport.msgestiontickets.servicetest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bestsupport.msgestiontickets.model.Status;
import com.bestsupport.msgestiontickets.model.Ticket;
import com.bestsupport.msgestiontickets.repository.TicketRepository;
import com.bestsupport.msgestiontickets.service.TicketService;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Test
    void getAllTickets_FullList_ShouldReturnAllTickets() {

        //Given
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        Ticket ticket3 = new Ticket();
        List<Ticket> tickets = List.of(ticket1, ticket2, ticket3);
        when(ticketRepository.findAll()).thenReturn(tickets);

        //When
        List<Ticket> result = ticketService.getAllTickets();

        //Then
        assert result.size() == 3;
        assert result.contains(ticket1);
        verify(ticketRepository).findAll();
    }

    @Test
    void getAllTickets_EmptyList_ShouldReturnEmptyList() {

        //Given
        List<Ticket> tickets = List.of();
        when(ticketRepository.findAll()).thenReturn(tickets);

        //When
        List<Ticket> result = ticketService.getAllTickets();

        //Then
        assert result.isEmpty();
        verify(ticketRepository).findAll();
    }

    @Test
    void getTicketById_ValidId_ShouldReturnTicket() {

        //Given
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        //When
        Ticket result = ticketService.getTicketById(1L);

        //Then
        assert result != null;
        assert result.getId() == 1L;
        verify(ticketRepository).findById(1L);
    }

    @Test
    void getTicketById_InvalidId_ShouldReturnNull() {

        //Given
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        //When
        Ticket result = ticketService.getTicketById(1L);

        //Then
        assert result == null;
        verify(ticketRepository).findById(1L);
    }

    @Test
    void createTicket_ValidTicket_ShouldReturnCreatedTicket() {

        //Given
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        //When
        Ticket result = ticketService.createTicket(ticket);

        //Then
        assert result != null;
        assert result.getId() == 1L;
        verify(ticketRepository).save(ticket);
    }

    @Test
    void updateTicket_ValidId_ShouldReturnUpdatedTicket() {

        //Given
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(ticketRepository.existsById(1L)).thenReturn(true);
        when(ticketRepository.save(ticket)).thenReturn(ticket);

        //When
        Ticket result = ticketService.updateTicket(1L, ticket);

        //Then
        assert result != null;
        assert result.getId() == 1L;
        verify(ticketRepository).existsById(1L);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void updateTicket_InvalidId_ShouldReturnNull() {

        //Given
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        when(ticketRepository.existsById(1L)).thenReturn(false);

        //When
        Ticket result = ticketService.updateTicket(1L, ticket);

        //Then
        assert result == null;
        verify(ticketRepository).existsById(1L);
    }

    @Test
    void deleteTicket_ValidId_ShouldReturnTrue() {

        //Given
        when(ticketRepository.existsById(1L)).thenReturn(true);

        //When
        boolean result = ticketService.deleteTicket(1L);

        //Then
        assert result;
        verify(ticketRepository).existsById(1L);
        verify(ticketRepository).deleteById(1L);
    }

    @Test
    void deleteTicket_InvalidId_ShouldReturnFalse() {

        //Given
        when(ticketRepository.existsById(1L)).thenReturn(false);

        //When
        boolean result = ticketService.deleteTicket(1L);

        //Then
        assert !result;
        verify(ticketRepository).existsById(1L);
    }

    @Test
    void getTicketsByStatus_ValidStatus_ShouldReturnTickets() {

        //Given
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        List<Ticket> tickets = List.of(ticket1, ticket2);
        when(ticketRepository.findByStatus(Status.valueOf("ABIERTO"))).thenReturn(tickets);

        //When
        List<Ticket> result = ticketService.getTicketsByStatus(Status.valueOf("ABIERTO"));

        //Then
        assert result.size() == 2;
        assert result.contains(ticket1);
        verify(ticketRepository).findByStatus(Status.valueOf("ABIERTO"));
    }
}
