package com.meeting.meetingplanner.repository.repositoryImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeting.meetingplanner.domain.Room;
import com.meeting.meetingplanner.repository.repository.RoomRepository;

@Service
public class RoomRepositoryImpl implements RoomRepository {

	@Override
	public List<Room> findAllrooms() {
		ObjectMapper mapper = new ObjectMapper();
		TypeReference<List<Room>> typeReference = new TypeReference<List<Room>>(){};
		InputStream inputStream = TypeReference.class.getResourceAsStream("/json/roomsList.json");
		try {
			return mapper.readValue(inputStream,typeReference);
		} catch (IOException e){
			System.out.println(e.getMessage());
			return null;
		}
	}

}
