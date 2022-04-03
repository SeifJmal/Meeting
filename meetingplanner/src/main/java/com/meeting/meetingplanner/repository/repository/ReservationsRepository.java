package com.meeting.meetingplanner.repository.repository;

import java.util.List;

import org.springframework.stereotype.Service;
import com.meeting.meetingplanner.domain.Reservation;
@Service
public interface ReservationsRepository {
public List<Reservation> findAllReservation();
}
