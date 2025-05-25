package com.bestsupport.msgestiontickets.controllertest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.bestsupport.msgestiontickets.controller.TicketController;
import com.bestsupport.msgestiontickets.dto.TicketDTO;
import com.bestsupport.msgestiontickets.dto.TicketDTOConverter;
import com.bestsupport.msgestiontickets.model.Status;
import com.bestsupport.msgestiontickets.model.Ticket;
import com.bestsupport.msgestiontickets.model.TicketCategory;
import com.bestsupport.msgestiontickets.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketService ticketService;

    @MockitoBean
    private TicketDTOConverter ticketDTOConverter;

    private static final Logger logger = LoggerFactory.getLogger(TicketController.class);

    @Test
    void getServer_GetRequest_Ok() throws Exception {

        // Given
        String URI = "/api/v1/tickets/server";

        // When
        MvcResult response = mockMvc.perform(get(URI)).andReturn();

        logger.info("Status: " + response.getResponse().getStatus());
        logger.info("Response: " + response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();

        assertEquals(200, responseStatus, "Status should be 200");
        assertEquals("Ticket Service is running", responseBody, "Response body should be 'Ticket Service is running'");

    }

    @Test
    void getServer_GetRequest_Ok_Optimized() throws Exception {

        mockMvc.perform(get("/api/v1/tickets/server"))
                .andExpect(status().isOk())
                .andExpect(content().string("Ticket Service is running"));
    }

    @Test
    void getAllTickets_EmptyList_NoContent() throws Exception {
        // Given
        String URI = "/api/v1/tickets";

        List<Ticket> emptyList = new ArrayList<>();
        when(ticketService.getAllTickets()).thenReturn(emptyList);

        // When
        MvcResult response = mockMvc.perform(get(URI)).andReturn();

        logger.info("Status: {}", response.getResponse().getStatus());
        logger.info("Response: {}", response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();

        assertEquals(HttpStatus.NO_CONTENT.value(), responseStatus, "Status should be 204");
        assertTrue(responseBody.isEmpty(), "Response body should be empty");
    }

    @Test
    void getAllTickets_FullList_Ok() throws Exception {
        // Given
        String URI = "/api/v1/tickets";

        // Mock data
        Ticket ticket1 = new Ticket();
        ticket1.setTitle("Test Ticket 1");

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTitle("Test Ticket DTO 1");

        List<TicketDTO> ticketDTOList = new ArrayList<>();
        ticketDTOList.add(ticketDTO);

        // Mock services responses
        when(ticketService.getAllTickets()).thenReturn(List.of(ticket1));
        when(ticketDTOConverter.convert(ticket1)).thenReturn(ticketDTO);

        // When
        MvcResult response = mockMvc.perform(get(URI)).andReturn();

        logger.info("Status: {}", response.getResponse().getStatus());
        logger.info("Response: {}", response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();

        assertEquals(HttpStatus.OK.value(), responseStatus, "Status should be 200");
        assertFalse(responseBody.isEmpty(), "Response body should not be empty");
        assertTrue(responseBody.contains("Test Ticket DTO 1"), "Response body should contain 'Test Ticket DTO 1'");
    }

    @Test
    void getTicketById_ValidId_Ok() throws Exception {
        // Given
        String URI = "/api/v1/tickets/1";

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Test Ticket");

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketId(1L);
        ticketDTO.setTitle("Test Ticket DTO");

        // Mock services responses
        when(ticketService.getTicketById(1L)).thenReturn(ticket);
        when(ticketDTOConverter.convert(ticket)).thenReturn(ticketDTO);

        // When
        MvcResult response = mockMvc.perform(get(URI)).andReturn();

        logger.info("Status: {}", response.getResponse().getStatus());
        logger.info("Response: {}", response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();

        assertEquals(HttpStatus.OK.value(), responseStatus, "Status should be 200");
        assertFalse(responseBody.isEmpty(), "Response body should not be empty");
        assertTrue(responseBody.contains("Test Ticket DTO"), "Response body should contain 'Test Ticket DTO'");
    }

    @Test
    void getTicketById_InvalidId_NotFound() throws Exception {
        // Given
        String URI = "/api/v1/tickets/999";

        // Mock services responses
        when(ticketService.getTicketById(999L)).thenReturn(null);

        // When
        MvcResult response = mockMvc.perform(get(URI)).andReturn();

        logger.info("Status: {}", response.getResponse().getStatus());
        logger.info("Response: {}", response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseStatus, "Status should be 404");
        assertTrue(responseBody.isEmpty(), "Response body should be empty");
    }

    @Test
    void getTicketsByStatus_ValidStatus_Ok() throws Exception {
        // Given
        String URI = "/api/v1/tickets/status/ABIERTO";

        Ticket ticket1 = new Ticket();
        ticket1.setId(1L);
        ticket1.setTitle("Open Ticket 1");
        ticket1.setStatus(Status.ABIERTO);

        TicketDTO ticketDTO1 = new TicketDTO();
        ticketDTO1.setTicketId(1L);
        ticketDTO1.setTitle("Open Ticket DTO 1");

        List<Ticket> tickets = Arrays.asList(ticket1);

        // Mock services responses
        when(ticketService.getTicketsByStatus(Status.ABIERTO)).thenReturn(tickets);
        when(ticketDTOConverter.convert(ticket1)).thenReturn(ticketDTO1);

        // When
        MvcResult response = mockMvc.perform(get(URI)).andReturn();

        logger.info("Status: {}", response.getResponse().getStatus());
        logger.info("Response: {}", response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();

        assertEquals(HttpStatus.OK.value(), responseStatus, "Status should be 200");
        assertFalse(responseBody.isEmpty(), "Response body should not be empty");
        assertTrue(responseBody.contains("Open Ticket DTO 1"), "Response body should contain 'Open Ticket DTO 1'");
    }

    @Test
    void getTicketsByStatus_EmptyList_NoContent() throws Exception {
        // Given
        String URI = "/api/v1/tickets/status/CERRADO";

        // Mock services responses
        when(ticketService.getTicketsByStatus(Status.CERRADO)).thenReturn(new ArrayList<>());

        // When
        MvcResult response = mockMvc.perform(get(URI)).andReturn();

        logger.info("Status: {}", response.getResponse().getStatus());
        logger.info("Response: {}", response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();

        assertEquals(HttpStatus.NO_CONTENT.value(), responseStatus, "Status should be 204");
        assertTrue(responseBody.isEmpty(), "Response body should be empty");
    }

    @Test
    void getTicketsByStatus_InvalidStatus_BadRequest() throws Exception {
        // Given
        String URI = "/api/v1/tickets/status/INVALID_STATUS";

        // When
        MvcResult response = mockMvc.perform(get(URI)).andReturn();

        logger.info("Status: {}", response.getResponse().getStatus());
        logger.info("Response: {}", response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();

        assertEquals(HttpStatus.BAD_REQUEST.value(), responseStatus, "Status should be 400");
        assertTrue(responseBody.isEmpty(), "Response body should be empty");
    }

    @Test
    void createTicket_ValidTicket_Created() throws Exception {
        // Given
        String URI = "/api/v1/tickets";

        Ticket ticket = new Ticket();

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTitle("New Ticket DTO");

        String body = new ObjectMapper().writeValueAsString(ticketDTO);

        // Mock services responses
        when(ticketService.createTicket(ticket)).thenReturn(ticket);
        when(ticketDTOConverter.convert(ticket)).thenReturn(ticketDTO);
        when(ticketDTOConverter.convertToEntity(ticketDTO)).thenReturn(ticket);

        // When
        MvcResult response = mockMvc.perform(post(URI)
                .contentType("application/json")
                .content(body))
                .andReturn();
        
        logger.info("Status: {}", response.getResponse().getStatus());
        logger.info("Response: {}", response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();
        assertEquals(HttpStatus.CREATED.value(), responseStatus, "Status should be 201");
        assertFalse(responseBody.isEmpty(), "Response body should not be empty");
        assertTrue(responseBody.contains("New Ticket DTO"), "Response body should contain 'New Ticket DTO'");
    }

    @Test
    void updateTicket_ValidTicket_Ok() throws Exception {
        // Given
        String URI = "/api/v1/tickets/1";

        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTitle("Updated Ticket");

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketId(1L);
        ticketDTO.setTitle("Updated Ticket DTO");

        String body = new ObjectMapper().writeValueAsString(ticketDTO);

        // Mock services responses
        when(ticketService.updateTicket(1L, ticket)).thenReturn(ticket);
        when(ticketDTOConverter.convertToEntity(ticketDTO)).thenReturn(ticket);
        when(ticketDTOConverter.convert(ticket)).thenReturn(ticketDTO);

        // When
        MvcResult response = mockMvc.perform(put(URI)
                .contentType("application/json")
                .content(body))
                .andReturn();

        logger.info("Status: {}", response.getResponse().getStatus());
        logger.info("Response: {}", response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();
        
        assertEquals(HttpStatus.OK.value(), responseStatus, "Status should be 200");
        assertFalse(responseBody.isEmpty(), "Response body should not be empty");
        assertTrue(responseBody.contains("Updated Ticket DTO"), "Response body should contain 'Updated Ticket DTO'");
    }

    @Test
    void updateTicket_InvalidId_NotFound() throws Exception {
        // Given
        String URI = "/api/v1/tickets/999";

        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setTicketId(999L);
        ticketDTO.setTitle("Non-existent Ticket DTO");

        String body = new ObjectMapper().writeValueAsString(ticketDTO);

        // Mock services responses
        when(ticketService.updateTicket(999L, null)).thenReturn(null);

        // When
        MvcResult response = mockMvc.perform(put(URI)
                .contentType("application/json")
                .content(body))
                .andReturn();

        logger.info("Status: {}", response.getResponse().getStatus());
        logger.info("Response: {}", response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseStatus, "Status should be 404");
        assertTrue(responseBody.isEmpty(), "Response body should be empty");
    }

    @Test
    void deleteTicket_ValidId_Ok() throws Exception {
        // Given
        String URI = "/api/v1/tickets/1";

        // Mock services responses
        when(ticketService.deleteTicket(1L)).thenReturn(true);

        // When
        MvcResult response = mockMvc.perform(delete(URI)).andReturn();

        logger.info("Status: {}", response.getResponse().getStatus());
        logger.info("Response: {}", response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();

        assertEquals(HttpStatus.NO_CONTENT.value(), responseStatus, "Status should be 204");
        assertTrue(responseBody.isEmpty(), "Response body should be empty");

        verify(ticketService).deleteTicket(1L);
    }

    @Test
    void deleteTicket_InvalidId_NotFound() throws Exception {
        // Given
        String URI = "/api/v1/tickets/999";

        // Mock services responses
        when(ticketService.deleteTicket(999L)).thenReturn(false);

        // When
        MvcResult response = mockMvc.perform(delete(URI)).andReturn();

        logger.info("Status: {}", response.getResponse().getStatus());
        logger.info("Response: {}", response.getResponse().getContentAsString());

        // Then
        int responseStatus = response.getResponse().getStatus();
        String responseBody = response.getResponse().getContentAsString();

        assertEquals(HttpStatus.NOT_FOUND.value(), responseStatus, "Status should be 404");
        assertTrue(responseBody.isEmpty(), "Response body should be empty");

        verify(ticketService).deleteTicket(999L);
    }
}
