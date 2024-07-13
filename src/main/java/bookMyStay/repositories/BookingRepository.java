package bookMyStay.repositories;

import bookMyStay.entities.Booking;
import bookMyStay.entities.Room;
import bookMyStay.entities.Status;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> getAllByStatusAndModerator_Id(Status status, Long moderatorId, Sort sort);

    List<Booking> getAllByStatusAndGuest_Id(Status status, Long guestId, Sort sort);

    @Query("select b.id from Booking b where b.startDate >= current_date and b.status = 'ACCEPTED'")
    Set<Long> getActualIds();

    @Query("select b.room.id from Booking b where (b.startDate >= current_date and b.status = 'ACCEPTED') "
            + "and (:start between b.startDate and b.endDate "
            + "or :end between b.startDate and b.endDate "
            + "or b.startDate between :start and :end "
            + "or b.endDate between :start and :end)")
    Set<Long> getActualIds(Timestamp start, Timestamp end);

    void deleteAllByRoom(Room room);

    @Query("select b.room.id from Booking b where (b.startDate >= current_date and b.status = 'ACCEPTED') "
            + "and (:start between b.startDate and b.endDate "
            + "or :end between b.startDate and b.endDate "
            + "or b.startDate between :start and :end "
            + "or b.endDate between :start and :end) "
            + "and b.guest.id = :id")
    Set<Long> getActualIdsByGuest(Timestamp start, Timestamp end, Long id);
}
