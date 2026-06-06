package com.phuc.datvekhachsan.model;

public class Room {
    public enum RoomStatus { AVAILABLE, SELECTED, UNAVAILABLE }

    private RoomStatus status;
    private String name;

    public Room(RoomStatus status, String name) {
        this.status = status;
        this.name = name;
    }

    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }
    public String getName() { return name; }
}
