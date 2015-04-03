package fr.devoxx.javaeeasy.services.business;

import fr.devoxx.javaeeasy.models.event.SlotUpdateEvent;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@ApplicationScoped
public class SlotBackgroundService {
    @PersistenceContext(unitName = "devoxx")
    private EntityManager em;


    /*
     * Sauve toutes les nouveaux slot dans les entités SlotJpa sur réception de l'event
     */
    @Transactional
    public void saveConferences(@Observes final SlotUpdateEvent conferences) {

    }
}



