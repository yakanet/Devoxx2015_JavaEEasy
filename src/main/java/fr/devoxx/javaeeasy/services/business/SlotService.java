package fr.devoxx.javaeeasy.services.business;

import fr.devoxx.javaeeasy.services.jpa.SlotJpa;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Map;

@ApplicationScoped
public class SlotService {
    @PersistenceContext(unitName = "devoxx")
    private EntityManager em;


    @Transactional
    public SlotJpa findById(final String id) {
        return null;
    }

    /*
     * Retourne le nombre d'attendee pour le slot passé en paramètre
     */
    public int count(final SlotJpa slot) {
        return -1;
    }

    /*
     * Retourne une map dont les clefs sont les slotId
     * et les valeur le nombre d'Attendees sur chacun de ces slot.
     */
    public Map<String, Integer> getAttendeesCountGroupedBySlotId() {
        return null;
    }


}
