package ru.ssau.carshwebcourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ssau.carshwebcourse.entity.Drive;
import ru.ssau.carshwebcourse.entity.DriveStatus;
import ru.ssau.carshwebcourse.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface DriveRepository extends JpaRepository<Drive,Long> {
    Drive getDriveByDriveId(Long driveId);

    @Query(value = "select d from Drive d where (d.driveStatus = COMPLETED or d.driveStatus = CANCELLED) and d.user = :user")
    List<Drive> findDriveHistoryByUser(User user);

    @Query(value = "select d from Drive d where (d.driveStatus = ACTIVE or d.driveStatus = PENDING_PAYMENT) and d.user = :user")
    List<Drive> findDriveByDriveStatusAndUser(User user);

    Long countDrivesByDriveStatusAndUser_UserId(DriveStatus driveStatus, Long userUserId);
}
