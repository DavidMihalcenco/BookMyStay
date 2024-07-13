package bookMyStay.dtos;

import lombok.Builder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

@Builder
public record ViewRequest(
        Long id,
        Long roomId,
        BigDecimal price,
        Integer nrGuests,
        LocalDate startDate,
        LocalDate endDate,
        String guestEmail,
        String ownerEmail
) {}
