package bookMyStay.dtos;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RoomDto(
  BigDecimal price,
  Integer nrOfGuests
){}
