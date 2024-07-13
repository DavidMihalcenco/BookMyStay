package bookMyStay.dtos;

import java.io.Serializable;

/**
 * A DTO for the {@link bookMyStay.entities.Hotel} entity
 */
public record HotelDto(Long id, String name, String location, String phoneNumber,
                       String ownerEmail) implements Serializable {
}