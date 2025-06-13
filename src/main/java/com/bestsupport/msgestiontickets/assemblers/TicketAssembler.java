package com.bestsupport.msgestiontickets.assemblers;

import com.bestsupport.msgestiontickets.controller.TicketController;
import com.bestsupport.msgestiontickets.dto.TicketCategoryDTO;
import com.bestsupport.msgestiontickets.dto.TicketDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TicketAssembler implements RepresentationModelAssembler<TicketDTO, EntityModel<TicketDTO>> {

    @Override
    public EntityModel<TicketDTO> toModel(@NonNull TicketDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(TicketController.class).getTicketById(dto.getTicketId())).withSelfRel(),
                linkTo(methodOn(TicketController.class).getAllTickets()).withRel("tickets"),
                linkTo(methodOn(TicketController.class).updateTicket(dto.getTicketId(), null)).withRel("update"),
                linkTo(methodOn(TicketController.class).deleteTicket(dto.getTicketId())).withRel("delete")
        );
    }
}
