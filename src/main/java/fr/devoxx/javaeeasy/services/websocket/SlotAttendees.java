package fr.devoxx.javaeeasy.services.websocket;

public class SlotAttendees {
    private String slotId;
    private int attendees;

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(final String slotId) {
        this.slotId = slotId;
    }

    public int getAttendees() {
        return attendees;
    }

    public void setAttendees(final int attendees) {
        this.attendees = attendees;
    }
}
