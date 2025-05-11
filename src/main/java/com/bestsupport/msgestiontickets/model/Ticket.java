package com.bestsupport.msgestiontickets.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    private String title;

    @Column(name = "ticket_description")
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Long clientId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private TicketCategory ticketCategory;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @Builder.Default
    private Date createdAt = new Date();

}
