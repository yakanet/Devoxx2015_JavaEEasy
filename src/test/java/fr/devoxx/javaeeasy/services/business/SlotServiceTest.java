package fr.devoxx.javaeeasy.services.business;


import fr.devoxx.javaeeasy.producers.ConfigurationProducer;
import fr.devoxx.javaeeasy.models.cfp.Slot;
import fr.devoxx.javaeeasy.models.event.SlotUpdateEvent;
import fr.devoxx.javaeeasy.producers.LoggerProducer;
import fr.devoxx.javaeeasy.mock.DbCleaner;
import fr.devoxx.javaeeasy.services.jpa.AttendeeJpa;
import fr.devoxx.javaeeasy.services.jpa.SlotJpa;
import org.apache.openejb.core.security.SecurityServiceImpl;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.spi.SecurityService;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Component;
import org.apache.openejb.testing.Descriptor;
import org.apache.openejb.testing.Descriptors;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.Principal;
import java.util.Date;
import java.util.Map;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(ApplicationComposer.class)
@Classes(context = "app", cdi = true, value = {
        SlotService.class,SlotBackgroundService.class, LoggerProducer.class, ConfigurationProducer.class, DbCleaner.class
})
@Descriptors({
        @Descriptor(name = "persistence.xml", path = "src/main/webapp/WEB-INF/persistence.xml")
})
public class SlotServiceTest {
    @PersistenceContext(unitName = "devoxx")
    private EntityManager em;

    @Inject
    private Event<SlotUpdateEvent> conferencesEvent;

    @Inject
    private UserTransaction ut;

    @Inject
    private SlotService slotService;

    // @{see securityService()}
    private String name;

    @Test
    public void persistenceOnEvent() {
        final Slot conference = new Slot();
        conference.setSlotId("slot_" + new Date());
        conferencesEvent.fire(new SlotUpdateEvent(asList(conference)));

        final SlotJpa slot = em.find(SlotJpa.class, conference.getSlotId());
        assertNotNull(slot);
        assertEquals(0, slotService.count(slot));
    }

    @Test
    public void getAttendeesBySlotId() throws Exception{
        SlotJpa slot1 = new SlotJpa();
        slot1.setId("slot1");
        SlotJpa slot2 = new SlotJpa();
        slot2.setId("slot2");
        ut.begin();
        em.persist(slot1);
        em.persist(slot2);
        ut.commit();
        //
        AttendeeJpa attendeeJpa1 = new AttendeeJpa();
        attendeeJpa1.getSlot().add(slot1);
        attendeeJpa1.getSlot().add(slot2);
        attendeeJpa1.setName("1");

        AttendeeJpa attendeeJpa2 = new AttendeeJpa();
        attendeeJpa2.getSlot().add(slot1);
        attendeeJpa2.setName("2");

        ut.begin();
        em.persist(attendeeJpa1);
        em.persist(attendeeJpa2);
        ut.commit();

        Map<String, Integer> attendeesBySlotId = slotService.getAttendeesCountGroupedBySlotId();

        assertNotNull(attendeesBySlotId);
        assertEquals(2,attendeesBySlotId.size());
        assertEquals(2,(int)attendeesBySlotId.get(slot1.getId()));
        assertEquals(1,(int)attendeesBySlotId.get(slot2.getId()));



    }

    @Component
    public SecurityService securityService() {
        return new SecurityServiceImpl() {
            @Override
            public Principal getCallerPrincipal() {
                return new User(name);
            }
        };
    }
}
