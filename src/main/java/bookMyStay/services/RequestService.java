package bookMyStay.services;

import bookMyStay.dtos.ViewRequest;
import bookMyStay.entities.*;
import bookMyStay.repositories.BookingRepository;
import bookMyStay.repositories.RequestRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Transactional
@AllArgsConstructor
@Service
public class RequestService {

    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    public List<ViewRequest> getPendingRequestsByModerator(User moderator) {
        return requestRepository.getAllByModerator_Id(moderator.getId(), Sort.by(Sort.Direction.ASC, "startDate"))
                .stream()
                .map(request -> ViewRequest.builder()
                        .id(request.getId())
                        .roomId(request.getRoom().getId())
                        .price(calculatePrice(request))
                        .nrGuests(request.getRoom().getNrGuests())
                        .startDate(request.getStartDate().toLocalDateTime().toLocalDate())
                        .endDate(request.getEndDate().toLocalDateTime().toLocalDate())
                        .guestEmail(request.getGuest().getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ViewRequest> getProcessedRequestByModerator(User moderator, Status status) {
        return bookingRepository.getAllByStatusAndModerator_Id(status, moderator.getId(), Sort.by(Sort.Direction.ASC, "startDate"))
                .stream()
                .map(request -> ViewRequest.builder()
                        .id(request.getId())
                        .roomId(request.getRoom().getId())
                        .price(calculatePrice(request))
                        .nrGuests(request.getRoom().getNrGuests())
                        .startDate(request.getStartDate().toLocalDateTime().toLocalDate())
                        .endDate(request.getEndDate().toLocalDateTime().toLocalDate())
                        .guestEmail(request.getGuest().getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    public void deleteRoomsRequests(Room room) {
        requestRepository.deleteAllByRoom(room);
        bookingRepository.deleteAllByRoom(room);
    }

    public void processRequest(Long id, Status status) {
        BookRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No requests with id: " + id));

        bookingRepository.save(Booking.builder()
                .guest(request.getGuest())
                .moderator(request.getModerator())
                .room(request.getRoom())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(status)
                .build());

        requestRepository.delete(request);
    }

    public List<ViewRequest> getProcessedRequestByGuest(User guest, Status status) {
        return bookingRepository.getAllByStatusAndGuest_Id(status, guest.getId(), Sort.by(Sort.Direction.ASC, "startDate"))
                .stream()
                .map(request -> ViewRequest.builder()
                        .id(request.getId())
                        .roomId(request.getRoom().getId())
                        .price(calculatePrice(request))
                        .nrGuests(request.getRoom().getNrGuests())
                        .startDate(request.getStartDate().toLocalDateTime().toLocalDate())
                        .endDate(request.getEndDate().toLocalDateTime().toLocalDate())
                        .guestEmail(request.getGuest().getEmail())
                        .ownerEmail(request.getModerator().getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ViewRequest> getPendingRequestsByGuest(User guest) {
        return requestRepository.getAllByGuest_Id(guest.getId(), Sort.by(Sort.Direction.ASC, "startDate"))
                .stream()
                .map(request -> ViewRequest.builder()
                        .id(request.getId())
                        .roomId(request.getRoom().getId())
                        .price(calculatePrice(request))
                        .nrGuests(request.getRoom().getNrGuests())
                        .startDate(request.getStartDate().toLocalDateTime().toLocalDate())
                        .endDate(request.getEndDate().toLocalDateTime().toLocalDate())
                        .guestEmail(request.getGuest().getEmail())
                        .ownerEmail(request.getModerator().getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    public void cancelRequest(Long id) {
        BookRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No requests with id: " + id));

        requestRepository.delete(request);
    }

    public Set<Long> getNotAvailableRoomIds() {
        Set<Long> ids = bookingRepository.getActualIds();
        ids.addAll(requestRepository.getActualIds());

        return ids;
    }

    public Set<Long> getNotAvailableRoomIds(Timestamp startDate, Timestamp endDate) {
        Set<Long> ids = bookingRepository.getActualIds(startDate, endDate);
        ids.addAll(requestRepository.getActualIds(startDate, endDate));

        return ids;
    }

    private Set<Long> getNotAvailableRoomIdsByGuest(Timestamp start, Timestamp end, Long id) {
        Set<Long> ids = bookingRepository.getActualIdsByGuest(start, end, id);
        ids.addAll(requestRepository.getActualIdsByGuest(start, end, id));

        return ids;
    }

    public void save(ViewRequest roomDto, Room room, User user) {
        Timestamp start = Timestamp.valueOf(roomDto.startDate().atStartOfDay());
        Timestamp end = Timestamp.valueOf(roomDto.endDate().atStartOfDay());
        Set<Long> ids = getNotAvailableRoomIds(start, end);
        if (ids.contains(room.getId()))
            throw new RuntimeException("Sorry, this room is already booked for this period");

        ids = getNotAvailableRoomIdsByGuest(start, end, user.getId());
        if (!ids.isEmpty())
            throw new RuntimeException("Sorry, you already have a reservation for this period");

        BookRequest newRequest = BookRequest.builder()
                .startDate(start)
                .endDate(end)
                .guest(user)
                .room(room)
                .moderator(room.getHotel().getOwner())
                .build();

        requestRepository.save(newRequest);
    }

    private BigDecimal calculatePrice(BookRequest request) {
        return calculatePrice(request.getStartDate(),
                request.getEndDate(),
                request.getRoom().getPrice());
    }

    private BigDecimal calculatePrice(Booking request) {
        return calculatePrice(request.getStartDate(),
                request.getEndDate(),
                request.getRoom().getPrice());
    }

    BigDecimal calculatePrice(Timestamp start, Timestamp end, BigDecimal price) {
        long millisecondsDiff = end.getTime() - start.getTime();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(millisecondsDiff);

        return price.multiply(new BigDecimal(daysDiff));
    }

}
