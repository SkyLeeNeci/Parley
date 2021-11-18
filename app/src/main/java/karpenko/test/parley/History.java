package karpenko.test.parley;

public class History {

    String roomCode, roomName,date;


    public History(){};

    public History(String roomCode, String roomName, String date) {
        this.roomCode = roomCode;
        this.roomName = roomName;
        this.date = date;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
