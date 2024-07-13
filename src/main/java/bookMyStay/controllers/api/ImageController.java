package bookMyStay.controllers.api;

import bookMyStay.services.HotelService;
import bookMyStay.services.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@RestController
@RequestMapping("/api/img")
public class ImageController {

    private final HotelService hotelService;
    private final RoomService roomService;

    @PostMapping(value = "/hotels/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void saveHotelImage(@PathVariable Long id, @RequestParam("imagefile") MultipartFile image) {
        hotelService.saveHotelImage(id, image);
    }

    @GetMapping(value = "/hotels/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] renderHotelImageFromDB(@PathVariable Long id) {
        return hotelService.getHotelImage(id);
    }

    @PostMapping(value = "/rooms/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void saveRoomImage(@PathVariable Long id, @RequestParam("imagefile") MultipartFile image) {
        roomService.saveRoomImage(id, image);
    }

    @GetMapping(value = "/rooms/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] renderRoomImageFromDB(@PathVariable Long id) {
        return roomService.getRoomImage(id);
    }
}
