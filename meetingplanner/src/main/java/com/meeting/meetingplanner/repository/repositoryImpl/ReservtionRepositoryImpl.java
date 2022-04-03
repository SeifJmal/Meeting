package com.meeting.meetingplanner.repository.repositoryImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeting.meetingplanner.domain.Reservation;
import com.meeting.meetingplanner.repository.repository.ReservationsRepository;
@Service
public class ReservtionRepositoryImpl implements ReservationsRepository {

	@Override
	public List<Reservation> findAllReservation() {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<Reservation>> typeReference = new TypeReference<List<Reservation>>(){};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/reservationsList.json");
		try {
			return mapper.readValue(inputStream,typeReference);
		} catch (IOException e){
			System.out.println(e.getMessage());
			return null;
		}
	}

}
