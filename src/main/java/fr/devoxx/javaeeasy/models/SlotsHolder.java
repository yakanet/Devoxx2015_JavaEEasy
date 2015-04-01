package fr.devoxx.javaeeasy.models;

import fr.devoxx.javaeeasy.models.cfp.Slot;
import fr.devoxx.javaeeasy.models.event.SlotUpdateEvent;

import java.util.ArrayList;
import java.util.Collection;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

@ApplicationScoped
public class SlotsHolder {
    private volatile Collection<Slot> conferences;

    public void update(@Observes final SlotUpdateEvent conferences) {
        this.conferences = conferences.getConferences();
    }

    public Collection<Slot> getConferences() {
        return conferences == null ? null : new ArrayList<>(conferences);
    }
}
