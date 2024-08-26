package com.odoo.RoomBooking.controller;

import com.odoo.RoomBooking.model.Room;
import com.odoo.RoomBooking.model.RoomBookingSchedule;
import com.odoo.RoomBooking.repository.RoomBookingScheduleRepository;
import com.odoo.RoomBooking.repository.RoomRepository;
import com.odoo.RoomBooking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class RoomController {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomBookingScheduleRepository roomBookingScheduleRepository;

    @Autowired
    RoomService roomService;

    @RequestMapping(value = "/rooms", method = RequestMethod.GET)
    public List<Room> getRooms(@RequestParam String filter){
        return roomRepository.findByNameOrTagsContaining(filter);
    }

    @RequestMapping(value = "/rooms/{id}/schedules", method = RequestMethod.GET)
    public List<RoomBookingSchedule> getRoomScheduleByDate(@PathVariable Integer id, @RequestParam(required = false) Date date){
        return roomBookingScheduleRepository.findByRoomIdAndDateOrderByStartTime(id, date);
    }

    @RequestMapping(value = "/rooms/{id}/schedules", method = RequestMethod.POST)
    public List<RoomBookingSchedule> bookRoom(@PathVariable Integer id,
                                              @RequestParam(required = false) Date date,
                                              @RequestBody List<RoomBookingSchedule> schedules){
        return roomService.bookRoom(id, date, schedules);
    }
}
