package com.bestsupport.msgestiontickets.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
public class TicketCategoryController {

    private final TicketCategoryService ticketCategoryService;
    private final TicketCategoryConverter ticketCategoryConverter;

    private static final Logger logger = LoggerFactory.getLogger(TicketCategoryController.class);

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok("Ticket Category Service is running");
    }
    
    @GetMapping()
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
