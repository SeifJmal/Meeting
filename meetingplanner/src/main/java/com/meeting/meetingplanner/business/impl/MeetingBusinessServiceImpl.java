package com.meeting.meetingplanner.business.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meeting.meetingplanner.business.services.MeetingBusinessService;
import com.meeting.meetingplanner.repository.repository.ReservationsRepository;
import com.meeting.meetingplanner.repository.repository.RoomRepository;
import com.meeting.meetingplanner.domain.Meeting;
import com.meeting.meetingplanner.domain.Reservation;
import com.meeting.meetingplanner.domain.Room;

@Service
public class MeetingBusinessServiceImpl implements MeetingBusinessService {

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private ReservationsRepository reservationsRepository;

	List<Room> rooms = new ArrayList<>();
	List<Reservation> reservations = new ArrayList<>();
	List<Meeting> meetings = new ArrayList<>();

	@Override
	public String getAllReservation() {
		this.reservations = reservationsRepository.findAllReservation();
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this.reservations);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getAllrooms() {
		this.rooms = roomRepository.findAllrooms();
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(this.rooms);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public String associateRommAndReservation() {
		ArrayList<Room> rooms = (ArrayList<Room>) roomRepository.findAllrooms();
		ArrayList<Reservation> reservations = (ArrayList<Reservation>) reservationsRepository.findAllReservation();
		this.reservations = reservations.stream().map(reservation -> {
			reservation.updateReservationEndTime();
			return reservation;
		}).collect(Collectors.toList());
		this.rooms = rooms.stream().map(room -> {
			room.calculateRealCapacityOfRooms();
			return room;
		}).collect(Collectors.toList());

		ArrayList<Reservation> vcTypeReservations = (ArrayList<Reservation>) this.getOrderedMeetingByTypeAndTime("VC");
		this.associateReservationToRoom(vcTypeReservations, "VC");
		ArrayList<Reservation> specTypeReservations = (ArrayList<Reservation>) this
				.getOrderedMeetingByTypeAndTime("SPEC");
		ArrayList<Reservation> rcTypeReservations = (ArrayList<Reservation>) this.getOrderedMeetingByTypeAndTime("RC");
		ArrayList<Reservation> specAndRcReservations = new ArrayList<Reservation>();
		specAndRcReservations.addAll(specTypeReservations);
		specAndRcReservations.addAll(rcTypeReservations);
		this.associateReservationToRoom(specAndRcReservations, "SPEC;RC");
		ArrayList<Reservation> rsTypeReservations = (ArrayList<Reservation>) this.getOrderedMeetingByTypeAndTime("RS");

		return null;
	}

	private List<Reservation> getOrderedMeetingByTypeAndTime(String type) {
		Comparator<Reservation> compareByStartOnAndMeetingOrder = Comparator.comparingInt(Reservation::getStartOn)
				.thenComparingInt(Reservation::getMeetingNumber);
		return this.reservations.stream().filter(elemnt -> elemnt.getMeetingType().equals(type))
				.sorted(compareByStartOnAndMeetingOrder).collect(Collectors.toList());
	}

	private void associateReservationToRoom(ArrayList<Reservation> reservations, String type) {
		if (type.equals("VC")) {
			ArrayList<Room> vcRooms = new ArrayList<>();
			for (Room room : this.rooms) {
				if (room.getRoomMaterial().contains("Webcam") && room.getRoomMaterial().contains("Pieuvre")
						&& room.getRoomMaterial().contains("Ecrant")) {
					vcRooms.add(room);
				}
			}
			vcRooms = (ArrayList<Room>) vcRooms.stream().sorted(Comparator.comparingInt(Room::getCapacity))
					.collect(Collectors.toList());
			for (Reservation reservation : reservations) {
				ArrayList<Reservation> previousReservation = (ArrayList<Reservation>) reservations.stream()
						.filter(elm -> elm.getMeetingNumber() < reservation.getMeetingNumber())
						.collect(Collectors.toList());
				int previousReservationsWithSameTimeNumber = previousReservation.stream()
						.filter(elm -> elm.getReservationEndTime() > reservation.getStartOn())
						.collect(Collectors.toList()).size();
				boolean allAvailablePrevious = onePreviousAreAvailavble(previousReservation, vcRooms);
				if (previousReservationsWithSameTimeNumber == 0 || !allAvailablePrevious) {
					for (Room room : vcRooms) {
						if (reservation.getParticipantNumber() <= room.getCapacity()
								&& reservation.getStartOn() >= room.getOccupoedTo()) {
							room.setOccupationTimeEnd(reservation.getReservationEndTime());
							Meeting meeting = new Meeting(reservation, room);
							meeting.createComment("OK");
							meetings.add(meeting);
						} else {
							Meeting meeting = new Meeting(reservation, null);
							meeting.createComment("Limited number of place");
							meetings.add(meeting);
						}
					}
				} else {
					Meeting meeting = new Meeting(reservation, null);
					meeting.createComment("Room Occupied in the same time");
					meetings.add(meeting);
				}
			}
		} else if (type.equals("SPEC;RC")) {
			int usedTable = 0;
			Comparator<Reservation> compareByStartOnAndMeetingOrder = Comparator.comparingInt(Reservation::getStartOn)
					.thenComparingInt(Reservation::getMeetingNumber);
			ArrayList<Reservation> sortedRcSpecReservations = (ArrayList<Reservation>) reservations.stream()
					.sorted(compareByStartOnAndMeetingOrder).collect(Collectors.toList());
			for (Reservation reservation : sortedRcSpecReservations) {
				if (usedTable < 2) {
					if (reservation.getMeetingType().equals("RC")) {
						Predicate<Room> predicate1 = elm -> elm.getRoomMaterial().contains("Ecran");
						Predicate<Room> predicate2 = elm -> elm.getRoomMaterial().contains("Pieuvre");
						ArrayList<Room> rcRooms = (ArrayList<Room>) this.rooms.stream()
								.filter(predicate1.and(predicate2)).collect(Collectors.toList()).stream()
								.sorted(Comparator.comparingInt(Room::getCapacity)).collect(Collectors.toList());
						for (Room room : rcRooms) {
							if (room.getCapacity() >= reservation.getParticipantNumber()) {
								this.rooms.stream().filter(rm -> rm.getRoomMaterial().contains(("Tableau")))
										.collect(Collectors.toList()).get(0).removeMaterial("Tableau");
								usedTable++;
								room.addMaterialToRomm("Tableau");
								Meeting meeting = new Meeting(reservation, room);
								meetings.add(meeting);
							}
						}

					} else if (reservation.getMeetingType().equals("SPEC")) {

					}
				}
			}

		} else if (type.equals("RS")) {

		} else {

		}

	}

	private boolean onePreviousAreAvailavble(ArrayList<Reservation> reservations, ArrayList<Room> rooms) {
		for (Reservation previous : reservations) {
			for (Room room2 : rooms) {
				if (previous.getParticipantNumber() <= room2.getCapacity()) {
					return true;
				}
			}
		}
		return false;
	}

}
