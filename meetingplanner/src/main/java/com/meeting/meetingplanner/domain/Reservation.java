package com.meeting.meetingplanner.domain;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
	@SuppressWarnings("unused")
	private int meetingNumber;
	@SuppressWarnings("unused")
	private String type;
	@SuppressWarnings("unused")
	private int personNumber;
	@SuppressWarnings("unused")
	private int startOn;
	@SuppressWarnings("unused")
	private int endOn;
	
	public int getStartOn() {
		return this.startOn;
	}
	public int updateReservationEndTime() {
		return this.endOn ++;
	}
	public int getReservationEndTime() {
		return this.endOn;
	}
	
	public int getMeetingNumber() {
		return this.meetingNumber;
	}
	
	public int getParticipantNumber() {
		return this.personNumber;
	}
	
	public String getMeetingType() {
		return this.type;
	}
	
}
