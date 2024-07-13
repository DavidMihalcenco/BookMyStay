package bookMyStay.controllers.api;

import bookMyStay.dtos.ViewRoomDto;
import bookMyStay.services.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final RoomService roomService;

    @GetMapping
    public List<ViewRoomDto> getRoomsWithFilter(@RequestParam BigDecimal min,
                                                @RequestParam BigDecimal max,
                                                @RequestParam LocalDate startDate,
                                                @RequestParam LocalDate endDate,
                                                @RequestParam String location,
                                                @RequestParam Integer nrOfGuests) {

        return roomService
                .getListViewRoomsWithFilters(min, max, startDate, endDate, location, nrOfGuests);
    }
}
