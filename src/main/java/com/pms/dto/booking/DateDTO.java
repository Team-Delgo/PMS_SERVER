package com.pms.dto.booking;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class DateDTO {
    private LocalDate date;
    private List<ReturnBookingDTO> list;
}
