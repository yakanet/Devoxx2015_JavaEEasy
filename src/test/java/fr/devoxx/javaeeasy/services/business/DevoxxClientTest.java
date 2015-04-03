package fr.devoxx.javaeeasy.services.business;

import fr.devoxx.javaeeasy.mock.BaseTestConfig;
import org.junit.Test;

import static fr.devoxx.javaeeasy.mock.DevoxxServerMock.assertConferences;

public class DevoxxClientTest extends BaseTestConfig {
    @Test
    public void retreiveSlots() {
        assertConferences(app.getInstance(JavaEEEasy.class).getClient().retrieveSlots());
    }

}
