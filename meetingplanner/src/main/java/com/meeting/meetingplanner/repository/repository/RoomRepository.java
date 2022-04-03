package com.meeting.meetingplanner.repository.repository;

import java.util.List;

import org.springframework.stereotype.Service;
import com.meeting.meetingplanner.domain.Room;
@Service
public interface RoomRepository {
	public List<Room> findAllrooms();
}
