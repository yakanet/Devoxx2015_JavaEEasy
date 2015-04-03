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
        return -1;
    }


    public Map<String, Integer> getAttendeesCountGroupedBySlotId() {
        return null;
    }


}
