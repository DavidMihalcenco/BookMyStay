package bookMyStay.controllers.api;

import bookMyStay.dtos.HotelDto;
import bookMyStay.dtos.RoomDto;
import bookMyStay.dtos.ViewHotelDto;
import bookMyStay.services.HotelService;
import jdk.jfr.ContentType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping({"/api/hotels", "/api/hotels/"})
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public List<HotelDto> getHotels() {
        return hotelService.getUserHotels();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addHotel(@RequestPart("hotelDto") HotelDto hotel, @RequestPart("img") MultipartFile img) {
        hotelService.save(hotel, img);
    }

    @PostMapping(value = "/{id}/rooms", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addRoom(@RequestPart("roomDto") RoomDto room,
                        @RequestPart("img")MultipartFile image,
                        @PathVariable Long id) {
        hotelService.save(room, image, id);
    }

    @GetMapping("/{id}")
    public ViewHotelDto getHotel(@PathVariable Long id) {
        return hotelService.getViewHotel(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public String deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotelById(id);
        return "success";
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/rooms/{roomId}")
    public String deleteRoom(@PathVariable Long roomId, @PathVariable Long id) {
        hotelService.deleteRoomById(roomId, id);
        return "success";
    }
}
