package com.odoo.RoomBooking.repository;

import com.odoo.RoomBooking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query(value = "SELECT r FROM Room r " +
            "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :input, '%')) " +
            " OR LOWER(r.tags) LIKE LOWER(CONCAT('%', :input, '%'))")
    List<Room> findByNameOrTagsContaining(@Param("input") String input);
}
