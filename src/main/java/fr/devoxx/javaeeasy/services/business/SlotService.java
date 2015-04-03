package fr.devoxx.javaeeasy.services.business;

import fr.devoxx.javaeeasy.services.jpa.AttendeeJpa;
import fr.devoxx.javaeeasy.services.jpa.SlotJpa;

import java.security.Principal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static java.util.stream.Collectors.toMap;

@ApplicationScoped
public class SlotService {
    @PersistenceContext(unitName = "devoxx")
    private EntityManager em;

    @Inject
    @RequestScoped
    private Principal principal;

    @Transactional
    public SlotJpa findById(final String id) {
        return em.find(SlotJpa.class, id);
    }

    public int count(final SlotJpa slot) {
        return em.createNamedQuery("Attendee.countBySlot", Number.class)
                .setParameter("slot", slot)
                .getSingleResult()
                .intValue();
    }

    @Transactional
    public Collection<String> getAllocatedSlotIdsForAttendee(String name) {
        Collection<String> slotIds = new LinkedList<>();
        AttendeeJpa attendee = findAttendee(name);
        if (attendee != null) {
            attendee.getSlot().forEach(slotJpa -> slotIds.add(slotJpa.getId()));
        }
        return slotIds;
    }

    public Map<String, Integer> getAttendeesCountGroupedBySlotId() {
        return em.createNamedQuery("Attendee.countGroupByAllSlot", Object[].class)
                .getResultList().stream()
                .collect(toMap(tuple -> (String) tuple[0], tuple -> Number.class.cast(tuple[1]).intValue()));
    }


    private AttendeeJpa findAttendee(String name) {
        try {
            return em.createNamedQuery("Attendee.findByName", AttendeeJpa.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (final NoResultException nre) {
            return null;
        }
    }


}
