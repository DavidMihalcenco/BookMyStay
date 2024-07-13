package bookMyStay.bootstrap;

import bookMyStay.entities.Hotel;
import bookMyStay.entities.Room;
import bookMyStay.services.HotelService;
import bookMyStay.services.ImageService;
import bookMyStay.services.RoomService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Transactional
@AllArgsConstructor
@Component
public class ImageBootstrap implements CommandLineRunner {

    private final ImageService imageService;
    private final HotelService hotelService;
    private final RoomService roomService;

    @Override
    public void run(String... args) throws Exception {
        bootstrapHotelsImages();
        bootstrapRoomsImages();
    }

    private void bootstrapRoomsImages() {
        Room room = roomService.findById(1L);

        if (room.getImage() != null || roomService.count() > 5)
            return;

        roomService.getAll().forEach(r -> {
            String str = r.getHotel().getName() + " " + r.getHotel().getLocation() + " " + r.getId();
            r.setImage(str.replaceAll(" ", "_") + ".jpg");
            roomService.save(r);
        });
    }

    private void bootstrapHotelsImages() {
        Hotel hotel = hotelService.findById(1L);

        if (hotel.getImage() != null || hotelService.count() > 5)
            return;

        hotelService.getAll().forEach(h -> {
            String str = h.getName() + " " + h.getLocation();
            h.setImage(str.replaceAll(" ", "_") + ".jpg");
            hotelService.save(h);
        });
    }


}
