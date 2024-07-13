package bookMyStay.dtos;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ViewRoomDto(
        Long id,
        BigDecimal price,
        Integer nrGuests,
        String hotelName,
        String location) {
}
