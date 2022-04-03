package com.meeting.meetingplanner.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meeting {
 @SuppressWarnings("unused")
private Room room;
 @SuppressWarnings("unused")
private Reservation reservation;
private String comments;
 
 public Meeting(Reservation reservation, Room room) {
	 this.reservation = reservation;
	 this.room = room;
 }
 
 public Room getMeetingRoom() {
	 return this.room;
 }
 public Reservation getMeetingReservation() {
	 return this.reservation;
 }
 
 public void createComment(String comment) {
	 this.comments = comment;
 }
 public String getComment() {
	 return this.comments;
 }
}
