package com.odoo.RoomBooking.repository;

import com.odoo.RoomBooking.model.RoomBookingSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Repository
public interface RoomBookingScheduleRepository extends JpaRepository<RoomBookingSchedule, Integer> {
    List<RoomBookingSchedule> findByRoomId(Integer roomId);

    List<RoomBookingSchedule> findByRoomIdAndDateOrderByStartTime(Integer roomId, Date date);

    @Query("SELECT rs FROM RoomBookingSchedule rs " +
            "WHERE rs.date = :date " +
            "AND (rs.roomId = :id)" +
            "AND ((:startTime BETWEEN rs.startTime AND rs.endTime) " +
            "OR (:endTime BETWEEN rs.startTime AND rs.endTime) " +
            "OR (rs.startTime BETWEEN :startTime AND :endTime))")
    List<RoomBookingSchedule> findConflictingSchedules(
            @Param("id") Integer id,
            @Param("date") Date date,
            @Param("startTime") Time startTime,
            @Param("endTime") Time endTime
    );
}
