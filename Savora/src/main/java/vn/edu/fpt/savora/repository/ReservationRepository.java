package vn.edu.fpt.savora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.savora.entity.Reservation;
import vn.edu.fpt.savora.enums.ReservationStatus;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> findByCustomer_UserIdOrderByReservationDateDescReservationTimeDesc(Integer userId);

    List<Reservation> findByDiningTable_TableIdAndReservationDateAndStatusIn(
            Integer tableId,
            LocalDate reservationDate,
            Collection<ReservationStatus> activeStatuses
    );

    Optional<Reservation> findByReservationIdAndCustomer_UserId(Integer reservationId, Integer userId);

    List<Reservation> findTop6ByOrderByCreatedAtDesc();
}
