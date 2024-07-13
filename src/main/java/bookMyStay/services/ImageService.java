package bookMyStay.services;

import bookMyStay.entities.Hotel;
import bookMyStay.entities.Room;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Getter
@AllArgsConstructor
@Service
public class ImageService {

    private final String DIR_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\img\\";

    @Transactional
    public void saveHotelImage(Hotel hotel, MultipartFile image) {
        String str = hotel.getName() + " " + hotel.getLocation();
        hotel.setImage(str.replaceAll(" ", "_") + ".jpg");
        String filePath = DIR_PATH + hotel.getImage();

        try {
            image.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Can't save image " + hotel.getImage(), e);
        }
    }

    public byte[] getHotelImage(Hotel hotel) {
        String filePath = DIR_PATH + hotel.getImage();

        try {
            return Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Can't load image " + hotel.getImage(), e);
        }
    }

    public byte[] getRoomImage(Room room) {
        String filePath = DIR_PATH + room.getImage();

        try {
            return Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Can't load image " + room.getImage(), e);
        }
    }

    public void saveRoomImage(Room room, MultipartFile image) {
        String str = room.getHotel().getName() + " " + room.getHotel().getLocation() + " " + room.getId();
        room.setImage(str.replaceAll(" ", "_") + ".jpg");
        String filePath = DIR_PATH + room.getImage();

        try {
            image.transferTo(new File(filePath));
        } catch (IOException e) {
            throw new RuntimeException("Can't save image " + room.getImage(), e);
        }
    }

    public void deleteImage(String image) {
        String filePath = DIR_PATH + image;

        try {
            Files.deleteIfExists(new File(filePath).toPath());
        } catch (IOException e) {
            throw new RuntimeException("No image was found" ,e);
        }
    }
}
