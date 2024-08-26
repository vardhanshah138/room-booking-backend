package com.odoo.RoomBooking;

import com.odoo.RoomBooking.model.Room;
import com.odoo.RoomBooking.model.RoomBookingSchedule;
import com.odoo.RoomBooking.repository.RoomBookingScheduleRepository;
import com.odoo.RoomBooking.repository.RoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class RoomBookingScheduleTests {

    @Autowired
    RoomBookingScheduleRepository roomBookingScheduleRepository;

    @Autowired
    RoomRepository roomRepository;

    @Test
    void getBookingScheduleById() {
        Room room = new Room();
        room.setName("Room For Scheduling");
        room.setCapacity(20);
        List<String> tags = new ArrayList<>();
        tags.add("AC");
        tags.add("TV");
        tags.add("Remote");
        room.setTags(tags);
        Room savedRoom = roomRepository.save(room);

        RoomBookingSchedule schedule = new RoomBookingSchedule();
        schedule.setRoomId(savedRoom.getId());
        schedule.setDate(Date.from(Instant.now()));
        schedule.setStartTime(Time.valueOf(LocalTime.of(6,30)));
        schedule.setEndTime(Time.valueOf(LocalTime.of(7,30)));

        RoomBookingSchedule schedule2 = new RoomBookingSchedule();
        schedule2.setRoomId(savedRoom.getId());
        schedule2.setDate(Date.from(Instant.now()));
        schedule2.setStartTime(Time.valueOf(LocalTime.of(7,30)));
        schedule2.setEndTime(Time.valueOf(LocalTime.of(8,30)));

        roomBookingScheduleRepository.save(schedule);
        roomBookingScheduleRepository.save(schedule2);

        List<RoomBookingSchedule> roomBookingSchedules = roomBookingScheduleRepository.findByRoomId(savedRoom.getId());
        Assertions.assertEquals(roomBookingSchedules.size(), 2);
    }

    @Test
    void getBookingScheduleByDate() {
        Room room = new Room();
        room.setName("Room For Scheduling");
        room.setCapacity(20);
        List<String> tags = new ArrayList<>();
        tags.add("AC");
        tags.add("TV");
        tags.add("Remote");
        room.setTags(tags);
        Room savedRoom = roomRepository.save(room);

        RoomBookingSchedule schedule = new RoomBookingSchedule();
        schedule.setRoomId(savedRoom.getId());
        schedule.setDate(Date.from(Instant.now()));
        schedule.setStartTime(Time.valueOf(LocalTime.of(6,30)));
        schedule.setEndTime(Time.valueOf(LocalTime.of(7,30)));

        RoomBookingSchedule schedule2 = new RoomBookingSchedule();
        schedule2.setRoomId(savedRoom.getId());
        schedule2.setDate(Date.from(Instant.now()));
        schedule2.setStartTime(Time.valueOf(LocalTime.of(7,30)));
        schedule2.setEndTime(Time.valueOf(LocalTime.of(8,30)));

        roomBookingScheduleRepository.save(schedule);
        roomBookingScheduleRepository.save(schedule2);

        List<RoomBookingSchedule> roomBookingSchedules = roomBookingScheduleRepository.findByRoomIdAndDateOrderByStartTime(savedRoom.getId(), Date.from(Instant.now()));
        Assertions.assertEquals(roomBookingSchedules.size(), 2);
    }

    @Test
    void testConflictOverlap() {
        Room room = new Room();
        room.setName("Room For Scheduling");
        room.setCapacity(20);
        List<String> tags = new ArrayList<>();
        tags.add("AC");
        tags.add("TV");
        tags.add("Remote");
        room.setTags(tags);
        Room savedRoom = roomRepository.save(room);

        RoomBookingSchedule schedule = new RoomBookingSchedule();
        schedule.setRoomId(savedRoom.getId());
        schedule.setDate(Date.from(Instant.now()));
        schedule.setStartTime(Time.valueOf(LocalTime.of(6,30)));
        schedule.setEndTime(Time.valueOf(LocalTime.of(7,30)));

        RoomBookingSchedule startOverlapSchedule = new RoomBookingSchedule();
        startOverlapSchedule.setRoomId(savedRoom.getId());
        startOverlapSchedule.setDate(Date.from(Instant.now()));
        startOverlapSchedule.setStartTime(Time.valueOf(LocalTime.of(7, 0)));
        startOverlapSchedule.setEndTime(Time.valueOf(LocalTime.of(8,30)));

        RoomBookingSchedule endOverlapSchedule = new RoomBookingSchedule();
        endOverlapSchedule.setRoomId(savedRoom.getId());
        endOverlapSchedule.setDate(Date.from(Instant.now()));
        endOverlapSchedule.setStartTime(Time.valueOf(LocalTime.of(7, 0)));
        endOverlapSchedule.setEndTime(Time.valueOf(LocalTime.of(7,15)));

        RoomBookingSchedule betweenOverlapSchedule = new RoomBookingSchedule();
        betweenOverlapSchedule.setRoomId(savedRoom.getId());
        betweenOverlapSchedule.setDate(Date.from(Instant.now()));
        betweenOverlapSchedule.setStartTime(Time.valueOf(LocalTime.of(6,45)));
        betweenOverlapSchedule.setEndTime(Time.valueOf(LocalTime.of(7,15)));

        roomBookingScheduleRepository.save(schedule);
        List<RoomBookingSchedule> roomBookingSchedules = roomBookingScheduleRepository.findByRoomIdAndDateOrderByStartTime(savedRoom.getId(), Date.from(Instant.now()));
        Assertions.assertEquals(roomBookingSchedules.size(), 1);

        List<RoomBookingSchedule> startOverlapSavedSchedule = roomBookingScheduleRepository.findConflictingSchedules(savedRoom.getId(), betweenOverlapSchedule.getDate(), betweenOverlapSchedule.getStartTime(), betweenOverlapSchedule.getEndTime());
        Assertions.assertEquals(startOverlapSavedSchedule.size(), 1);

        List<RoomBookingSchedule> endOverlapSavedSchedule = roomBookingScheduleRepository.findConflictingSchedules(savedRoom.getId(), betweenOverlapSchedule.getDate(), betweenOverlapSchedule.getStartTime(), betweenOverlapSchedule.getEndTime());
        Assertions.assertEquals(endOverlapSavedSchedule.size(), 1);

        List<RoomBookingSchedule> betweenOverlapSavedSchedule = roomBookingScheduleRepository.findConflictingSchedules(savedRoom.getId(), betweenOverlapSchedule.getDate(), betweenOverlapSchedule.getStartTime(), betweenOverlapSchedule.getEndTime());
        Assertions.assertEquals(betweenOverlapSavedSchedule.size(), 1);
    }
}
