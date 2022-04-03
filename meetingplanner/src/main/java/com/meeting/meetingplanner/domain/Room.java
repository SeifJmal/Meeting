package com.meeting.meetingplanner.domain;

import java.util.ArrayList;

import javax.persistence.Embedded;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {
	@SuppressWarnings("unused")
	private String roomName;
	@SuppressWarnings("unused")
	private int capacity;
	
	@Embedded
	private ArrayList<String> materials;
	private boolean isReserved;
	private int occupiedTo = 0;
	
	public void addMaterialToRomm(String material) {
		materials.add(material);	
	}
	public void removeMaterial(String material) {
			materials.remove(material);
	}
	public ArrayList<String> getRoomMaterial(){
		return this.materials;
	}
	
	public void reserveRoom() {
		this.isReserved = true;
	}
	public boolean isReserved() {
		return this.isReserved;
	}
	
	public void setOccupationTimeEnd(int endTime) {
		this.occupiedTo = endTime;
	}
	public int getOccupoedTo() {
		return this.occupiedTo;
	}
	public int calculateRealCapacityOfRooms() {
		float numberToRemove = (this.capacity * 30) / 100;
		this.capacity = this.capacity - (int) Math.round(numberToRemove);
		return this.capacity;
	}
	public int getCapacity() {
		return this.capacity;
	}
}
