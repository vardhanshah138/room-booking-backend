package com.odoo.RoomBooking;

import com.odoo.RoomBooking.model.Room;
import com.odoo.RoomBooking.model.RoomBookingSchedule;
import com.odoo.RoomBooking.repository.RoomBookingScheduleRepository;
import com.odoo.RoomBooking.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.sql.Time;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class RoomBookingApplicationTests {

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	RoomBookingScheduleRepository roomBookingScheduleRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void basic_entity_get_stored_in_db () {
		Room room = new Room();
		room.setName("room");
		room.setCapacity(50);

		Room savedRoom = roomRepository.save(room);
		Assert.isTrue(roomRepository.existsById(savedRoom.getId()), "Room not saved successfully");
		roomRepository.deleteById(savedRoom.getId());
		Assert.isTrue(!roomRepository.existsById(savedRoom.getId()), "Room not deleted successfully");
	}

	@Test
	void relational_entity_get_stored_in_db () {
		List<String> tags = new ArrayList<>();
		tags.add("A.C");

		Room room = new Room();
		room.setName("America");
		room.setCapacity(50);


		Room savedRoom = roomRepository.save(room);
		savedRoom.setTags(tags);
		savedRoom = roomRepository.save(savedRoom);
		Assert.isTrue(roomRepository.existsById(savedRoom.getId()), "Room not saved successfully");
	}

	@Test
	void room_booking_schedule_storage_test () {

		List<String> tags = new ArrayList<>();
		tags.add("Projector");
		tags.add("Monitor");

		Room room = new Room();
		room.setName("Asia");
		room.setCapacity(150);

		Room savedRoom = roomRepository.save(room);
		savedRoom.setTags(tags);
		savedRoom = roomRepository.save(savedRoom);
		Assert.isTrue(roomRepository.existsById(savedRoom.getId()), "Room not saved successfully");

		RoomBookingSchedule schedule = new RoomBookingSchedule();
		schedule.setDate(Date.from(OffsetDateTime.now().toInstant()));
		schedule.setStartTime(Time.valueOf(LocalTime.of(6,30)));
		schedule.setEndTime(Time.valueOf(LocalTime.of(7,30)));
		schedule.setRoomId(savedRoom.getId());

		RoomBookingSchedule savedSchedule = roomBookingScheduleRepository.save(schedule);
		Assert.isTrue(roomBookingScheduleRepository.existsById(savedSchedule.getId()), "Schedule not saved successfully");
	}

}
