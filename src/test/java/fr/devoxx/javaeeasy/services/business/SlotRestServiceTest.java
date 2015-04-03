package fr.devoxx.javaeeasy.services.business;

import fr.devoxx.javaeeasy.mock.BaseTestConfig;
import fr.devoxx.javaeeasy.models.cfp.Slot;
import fr.devoxx.javaeeasy.services.jpa.SlotJpa;
import org.apache.johnzon.jaxrs.JohnzonProvider;
import org.junit.Test;

import java.util.List;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import static fr.devoxx.javaeeasy.mock.DevoxxServerMock.assertConferences;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.junit.Assert.assertEquals;

public class SlotRestServiceTest extends BaseTestConfig {
    @Test
    public void getConferences() {
        assertConferences(
                ClientBuilder.newBuilder().build()
                        .register(new JohnzonProvider())
                        .target(container.getInstance(Container.class).getRoot().toExternalForm() + "app/rest/conferences")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .get(new GenericType<List<Slot>>() {
                        }));
    }

    @Test
    public void incrDecr() {
        final String slotId = "conf_room5_friday_14_9h30_10h30";
        final SlotService slots = app.getInstance(JavaEEEasy.class).getSlotService();

        final String authorization = "Basic " + printBase64Binary("jonathan:secret".getBytes());
        final WebTarget client = ClientBuilder.newBuilder().build()
                .target(container.getInstance(Container.class).getRoot().toExternalForm() + "app/rest/conferences/attendees");

        {
            final SlotJpa slot = slots.findById(slotId);
            assertEquals(0, slots.count(slot));
        }
        {
            client.path("increment/{slot}").resolveTemplate("slot", "conf_room5_friday_14_9h30_10h30")
                    .request()
                    .header("Authorization", authorization)
                    .head();
            assertEquals(1, slots.count(slots.findById(slotId)));
        }
        {
            client.path("decrement/{slot}").resolveTemplate("slot", "conf_room5_friday_14_9h30_10h30")
                    .request()
                    .header("Authorization", authorization)
                    .head();
            assertEquals(0, slots.count(slots.findById(slotId)));
        }
    }
}
