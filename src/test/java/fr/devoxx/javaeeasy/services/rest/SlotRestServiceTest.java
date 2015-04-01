package fr.devoxx.javaeeasy.services.rest;

import fr.devoxx.javaeeasy.mock.BaseTestConfig;
import fr.devoxx.javaeeasy.models.cfp.Slot;
import org.apache.johnzon.jaxrs.JohnzonProvider;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static fr.devoxx.javaeeasy.mock.DevoxxServerMock.assertConferences;

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


}
