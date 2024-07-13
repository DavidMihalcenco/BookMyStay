package bookMyStay.services;

import bookMyStay.dtos.RoomDto;
import bookMyStay.dtos.ViewRoomDto;
import bookMyStay.entities.Hotel;
import bookMyStay.entities.Room;
import bookMyStay.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Transactional
@AllArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final ImageService imageService;
    private final RequestService requestService;

    public List<ViewRoomDto> getListViewRoomsByHotelId(Long hotelId) {
        return roomRepository.getAllByHotelId(hotelId)
                .stream()
                .map(room -> ViewRoomDto.builder()
                        .id(room.getId())
                        .price(room.getPrice())
                        .nrGuests(room.getNrGuests())
                        .hotelName(room.getHotel().getName())
                        .location(room.getHotel().getLocation())
                        .build())
                .collect(Collectors.toList());
    }

    public Room findById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Room with id: " + id));
    }

    public Long count() {
        return roomRepository.count();
    }

    public List<Room> getAll() {
        return roomRepository.findAll();
    }

    public Room save(Room room) {
        return roomRepository.save(room);
    }

    public byte[] getRoomImage(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Room with id: " + id));

        return imageService.getRoomImage(room);
    }

    public void saveRoomImage(Long id, MultipartFile image) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Room with id: " + id));

        imageService.saveRoomImage(room, image);
        roomRepository.save(room);
    }

    public void save(RoomDto roomDto, MultipartFile image, Hotel hotel) {
        Room room = new Room(null, roomDto.price(), roomDto.nrOfGuests(), hotel, null);

        room = roomRepository.save(room);
        imageService.saveRoomImage(room, image);
        roomRepository.save(room);
    }

    public void deleteRoom(Long id, Long hotelId) {
        Room room = roomRepository.getRoomByIdAndHotel_Id(id, hotelId)
                .orElseThrow(() -> new RuntimeException("No Room with id: " + id + " in hotel with id: " + hotelId));

        imageService.deleteImage(room.getImage());
        requestService.deleteRoomsRequests(room);
        roomRepository.delete(room);
    }

    public void deleteHotelRooms(Long hotelId) {
        roomRepository.getAllByHotelId(hotelId)
                .forEach(r -> deleteRoom(r.getId(), hotelId));
    }

    public List<ViewRoomDto> getListViewRoomsWithFilters(BigDecimal min,
                                                         BigDecimal max,
                                                         LocalDate startDate,
                                                         LocalDate endDate,
                                                         String location,
                                                         Integer nrOfGuests) {

        Timestamp start = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp end = Timestamp.valueOf(endDate.atStartOfDay());
        Set<Long> ids = new HashSet<>(List.of(-1L));
        ids.addAll(requestService.getNotAvailableRoomIds(start, end));
        BigDecimal minPerDay = getPricePerDay(start, end, min);
        BigDecimal maxPerDay = getPricePerDay(start, end, max);

        return roomRepository.getAllByFilters(minPerDay, maxPerDay, ids, location, nrOfGuests)
                .stream()
                .map(r -> ViewRoomDto.builder()
                        .id(r.getId())
                        .price(requestService.calculatePrice(start, end, r.getPrice()))
                        .nrGuests(r.getNrGuests())
                        .location(r.getHotel().getLocation())
                        .hotelName(r.getHotel().getName())
                        .build())
                .collect(Collectors.toList());
    }

    private BigDecimal getPricePerDay(Timestamp start, Timestamp end, BigDecimal price) {
        long millisecondsDiff = end.getTime() - start.getTime();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(millisecondsDiff);
        return price.divide(new BigDecimal(daysDiff), 2, RoundingMode.DOWN);
    }

    public List<ViewRoomDto> getListAvailableRooms() {
        Set<Long> ids = requestService.getNotAvailableRoomIds();

        return roomRepository.getAllByIdNotIn(ids)
                .stream()
                .map(r -> ViewRoomDto.builder()
                        .id(r.getId())
                        .price(r.getPrice())
                        .nrGuests(r.getNrGuests())
                        .location(r.getHotel().getLocation())
                        .hotelName(r.getHotel().getName())
                        .build())
                .collect(Collectors.toList());
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No room was found with id: " + id));
    }
}
