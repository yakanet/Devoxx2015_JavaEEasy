package fr.devoxx.javaeeasy.models.event;

import fr.devoxx.javaeeasy.models.cfp.Slot;

import java.util.Collection;

public class SlotUpdateEvent {
    private final Collection<Slot> conferences;

    public SlotUpdateEvent(final Collection<Slot> conferences) {
        this.conferences = conferences;
    }

    public Collection<Slot> getConferences() {
        return conferences;
    }
}
