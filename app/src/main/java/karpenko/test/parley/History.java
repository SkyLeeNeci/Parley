package karpenko.test.parley;

public class History {

    String roomCode, roomName,roomDate;


    public History(){};

    public History(String roomCode, String roomName, String roomDate) {
        this.roomCode = roomCode;
        this.roomName = roomName;
        this.roomDate = roomDate;
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

    public String getRoomDate() {
        return roomDate;
    }

    public void setRoomDate(String roomDate) {
        this.roomDate = roomDate;
    }
}
