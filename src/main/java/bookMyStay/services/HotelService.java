package bookMyStay.services;

import bookMyStay.dtos.HotelDto;
import bookMyStay.dtos.RoomDto;
import bookMyStay.dtos.ViewHotelDto;
import bookMyStay.entities.Hotel;
import bookMyStay.entities.User;
import bookMyStay.repositories.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Transactional
@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private final SecurityService securityService;
    private final UserService userService;
    private final ImageService imageService;
    private final RoomService roomService;

    public List<HotelDto> getUserHotels() {
        String email = securityService.getClaim("email");
        return hotelRepository
                .findAllByOwner_Email(email)
                .stream()
                .map(h -> new HotelDto(h.getId(), h.getName(), h.getLocation(), h.getPhoneNumber(), h.getOwner().getEmail()))
                .collect(Collectors.toList());
    }

    public Hotel findById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No hotel with id " + id));
    }

    public void save(Hotel hotel) {
        hotelRepository.save(hotel);
    }

    public long count() {
        return hotelRepository.count();
    }

    public List<Hotel> getAll() {
        return hotelRepository.findAll();
    }

    public void save(HotelDto hotelDto, MultipartFile img) {
        String email = securityService.getClaim("email");
        User user = userService.findUserByEmail(email);
        Hotel hotel = new Hotel(null, hotelDto.name(), hotelDto.location(), hotelDto.phoneNumber(), user, null);

        hotel = hotelRepository.save(hotel);
        imageService.saveHotelImage(hotel, img);
        hotelRepository.save(hotel);
    }

    public void saveHotelImage(Long id, MultipartFile image) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);

        if (optionalHotel.isEmpty())
            throw new RuntimeException("No hotel with id " + id);

        Hotel hotel = optionalHotel.get();
        imageService.saveHotelImage(hotel, image);
        hotelRepository.save(hotel);
    }

    public byte[] getHotelImage(Long id) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(id);

        if (optionalHotel.isEmpty())
            throw new RuntimeException("No hotel with id " + id);

        Hotel hotel = optionalHotel.get();
        return imageService.getHotelImage(hotel);
    }

    public ViewHotelDto getViewHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No hotel with id " + id));

        return ViewHotelDto.builder()
                .id(hotel.getId())
                .ownerEmail(hotel.getOwner().getEmail())
                .location(hotel.getLocation())
                .name(hotel.getName())
                .phoneNumber(hotel.getPhoneNumber())
                .rooms(roomService.getListViewRoomsByHotelId(hotel.getId()))
                .build();
    }

    public void save(RoomDto room, MultipartFile image, Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No hotel with id " + id));

        roomService.save(room, image, hotel);
    }

    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No hotel with id " + id));

        imageService.deleteImage(hotel.getImage());
        roomService.deleteHotelRooms(hotel.getId());
        hotelRepository.delete(hotel);
    }

    public void deleteRoomById(Long roomId, Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No hotel with id " + id));

        roomService.deleteRoom(roomId, hotel.getId());
    }
}
