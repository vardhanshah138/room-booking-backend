package com.odoo.RoomBooking.service;

import com.odoo.RoomBooking.Exception.CommonException;
import com.odoo.RoomBooking.model.Room;
import com.odoo.RoomBooking.model.RoomBookingSchedule;
import com.odoo.RoomBooking.repository.RoomBookingScheduleRepository;
import com.odoo.RoomBooking.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomBookingScheduleRepository roomBookingScheduleRepository;

    @Transactional
    public List<RoomBookingSchedule> bookRoom(Integer id, Date date, List<RoomBookingSchedule> schedules){
        Optional<Room> room = roomRepository.findById(id);
        if(room.isEmpty()){
            throw new CommonException("Room doesn't exists");
        }

        List<RoomBookingSchedule> savedSchedules = new ArrayList<>();
        schedules.forEach(schedule -> {
            if(schedule != null){
                schedule.setRoomId(id);
                schedule.setDate(date);
                validateSchedule(schedule);
                savedSchedules.add(roomBookingScheduleRepository.save(schedule));
            }});
        return savedSchedules;
    }

    private void validateSchedule(RoomBookingSchedule schedule){
        if(schedule.getStartTime().after(schedule.getEndTime())){
            throw new CommonException("The start date comes after end date.");
        }
        List<RoomBookingSchedule> conflictSchedules = roomBookingScheduleRepository.findConflictingSchedules(schedule.getRoomId(), schedule.getDate(), schedule.getStartTime(), schedule.getEndTime());
        if(!conflictSchedules.isEmpty()){
            throw new CommonException("There are conflicts in schedule.");
        }
    }
}
