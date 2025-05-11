package com.bestsupport.msgestiontickets.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bestsupport.msgestiontickets.model.TicketCategory;
import com.bestsupport.msgestiontickets.repository.TicketCategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketCategoryService {

    private final static Logger logger = LoggerFactory.getLogger(TicketCategoryService.class);

    private final TicketCategoryRepository ticketCategoryRepository;

    public TicketCategory getTicketCategoryById(Long id) {
        return ticketCategoryRepository.findById(id)
                .orElse(null);
    }

    public List<TicketCategory> getAllTicketCategories() {
        logger.info("Fetching all ticket categories");
        List<TicketCategory> ticketCategories = ticketCategoryRepository.findAll();
        logger.info("Fetched {} ticket categories", ticketCategories.size());
        return ticketCategoryRepository.findAll();
    }
}
