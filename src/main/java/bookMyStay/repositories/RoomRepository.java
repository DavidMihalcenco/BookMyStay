package bookMyStay.repositories;

import bookMyStay.entities.Room;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> getAllByHotelId(Long hotelId);

    Optional<Room> getRoomByIdAndHotel_Id(Long id, Long hotelId);

    List<Room> getAllByIdNotIn(Set<Long> ids);

    void deleteRoomByHotel_Id(Long hotelId);

    List<Room> getAllByIdNotIn(Set<Long> ids, Sort sort);

    @Query("select r from Room r where r.price >= :min and r.price <= :max "
            + "and r.id not in :ids "
            + "and r.nrGuests >= :nrOfGuests "
            + "and r.hotel.location like %:location% "
            + "order by r.price")
    List<Room> getAllByFilters(BigDecimal min,
                               BigDecimal max,
                               Set<Long> ids,
                               String location,
                               Integer nrOfGuests);
}
