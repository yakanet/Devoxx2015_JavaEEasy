package fr.devoxx.javaeeasy.services.business;

import fr.devoxx.javaeeasy.models.event.SlotUpdateEvent;
import fr.devoxx.javaeeasy.services.jpa.SlotJpa;

import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@ApplicationScoped
public class SlotBackgroundService {
    @PersistenceContext(unitName = "devoxx")
    private EntityManager em;

    @Transactional
    public void saveConferences(@Observes final SlotUpdateEvent conferences) {
        // try to do it by "bulk" since we shouldn't have too much conf and it will avoid N queries for nothing
        final Map<String, SlotJpa> slots = em.createNamedQuery("Slot.findAll", SlotJpa.class)
                .getResultList().stream()
                .collect(toMap(SlotJpa::getId, identity()));

        conferences.getConferences().stream()
                .filter(s -> !slots.containsKey(s.getSlotId()))
                .map(c -> new SlotJpa(c.getSlotId()))
                .forEach(em::persist);
    }
}
