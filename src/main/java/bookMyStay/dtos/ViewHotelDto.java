package bookMyStay.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record ViewHotelDto(Long id, String name, String location, String phoneNumber,
                           String ownerEmail, List<ViewRoomDto> rooms) {
}
