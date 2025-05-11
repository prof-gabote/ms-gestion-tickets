package com.bestsupport.msgestiontickets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bestsupport.msgestiontickets.model.Ticket;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    
}
