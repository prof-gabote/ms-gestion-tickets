package com.bestsupport.msgestiontickets.dto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;

import com.bestsupport.msgestiontickets.model.Status;
import com.bestsupport.msgestiontickets.model.Ticket;
import com.bestsupport.msgestiontickets.repository.TicketCategoryRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TicketDTOConverter implements Converter<Ticket, TicketDTO> {

    private final TicketCategoryRepository ticketCategoryRepository;

    @Override
    @Nullable
    public TicketDTO convert(Ticket source) {
        if (source == null) {
            return null;
        }

        TicketDTO ticketDTO = TicketDTO.builder()
                .ticketId(source.getId())
                .title(source.getTitle())
                .description(source.getDescription())
                .status(source.getStatus().name())
                .clientId(source.getClientId())
                .categoryId(source.getTicketCategory().getId())
                .categoryName(source.getTicketCategory().getCategoryName())
                .createdAt(source.getCreatedAt().toString()) // Assuming createdAt is a LocalDateTime
                .build();

        return ticketDTO;
    }

    public Ticket convertToEntity(TicketDTO ticketDTO) {
        
        if (ticketDTO == null) {
            return null;
        }

        Ticket ticket = Ticket.builder()
                .id(ticketDTO.getTicketId())
                .title(ticketDTO.getTitle())
                .description(ticketDTO.getDescription())
                .status(Status.valueOf(ticketDTO.getStatus()))
                .clientId(ticketDTO.getClientId())
                .ticketCategory(ticketCategoryRepository.findById(ticketDTO.getCategoryId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Invalid category ID: " + ticketDTO.getCategoryId())))
                .build();

        return ticket;
    }

}
