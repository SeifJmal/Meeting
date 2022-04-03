package com.meeting.meetingplanner.business.services;

import java.util.List;

import org.springframework.stereotype.Service;
import com.meeting.meetingplanner.domain.*;
@Service
public interface MeetingBusinessService {
	public String getAllrooms();
	public String getAllReservation();
	public String associateRommAndReservation();
}
