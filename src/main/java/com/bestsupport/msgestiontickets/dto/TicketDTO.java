package com.bestsupport.msgestiontickets.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDTO {

    private Long ticketId;
    private String title;
    private String description;
    private String status;
    private Long clientId;
    private Long categoryId;
    private String categoryName;
    private String createdAt;
}
