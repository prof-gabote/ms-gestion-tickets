package com.bestsupport.msgestiontickets.dto;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import com.bestsupport.msgestiontickets.model.TicketCategory;

@Configuration
public class TicketCategoryConverter implements Converter<TicketCategory, TicketCategoryDTO> {

    @Override
    public TicketCategoryDTO convert(TicketCategory source) {
        if (source == null) {
            return null;
        }
        return TicketCategoryDTO.builder()
                .categoryId(source.getId())
                .categoryName(source.getCategoryName())
                .build();
    }

    public TicketCategory convert(TicketCategoryDTO source) {
        if (source == null) {
            return null;
        }
        return TicketCategory.builder()
                .id(source.getCategoryId())
                .categoryName(source.getCategoryName())
                .build();
    }

}
