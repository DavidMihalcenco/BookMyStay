package bookMyStay.dtos;

public record GetUserDto(
        Boolean isModerator,
        String email
) {
}
