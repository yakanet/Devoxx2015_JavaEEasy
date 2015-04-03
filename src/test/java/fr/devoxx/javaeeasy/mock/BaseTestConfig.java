package fr.devoxx.javaeeasy.mock;

import java.net.URL;

import javax.inject.Inject;

import fr.devoxx.javaeeasy.services.business.SlotService;
import org.apache.openejb.junit.ApplicationRule;
import org.apache.openejb.junit.ContainerRule;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.ContainerProperties;
import org.apache.openejb.testing.Default;
import org.apache.openejb.testing.EnableServices;
import org.apache.openejb.testing.RandomPort;
import org.junit.Rule;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import fr.devoxx.javaeeasy.services.business.DevoxxClient;

public class BaseTestConfig {
    protected final ContainerRule container = new ContainerRule(new Container());
    protected final ApplicationRule app = new ApplicationRule(new JavaEEEasy());

    @Rule
    public final TestRule rule = RuleChain
            .outerRule(container)
            .around(new ApplicationRule(new DevoxxApp())) // deploy it first for {@see ConferenceRetriever}
            .around(app);

    @Classes(context = "devoxx", value = DevoxxServerMock.class)
    public static class DevoxxApp {
    }

    @Default
    @Classes(context = "app", cdi = true, excludes = "fr.devoxx.javaeeasy.mock")
    public static class JavaEEEasy {
        @Inject
        private DevoxxClient client;

        @Inject
        private SlotService slotService;

        public SlotService getSlotService() {
            return slotService;
        }

        public DevoxxClient getClient() {
            return client;
        }
    }

    @EnableServices("jaxrs")
    @ContainerProperties({
        @ContainerProperties.Property(name = "client.url", value = "http://localhost:${httpejbd.port}/devoxx/api/"),
        @ContainerProperties.Property(name = "cxf-rs.auth", value = "BASIC") // GET are accepted even if not protected :)
    })
    public static class Container {
        @RandomPort("http")
        private URL root;

        public URL getRoot() {
            return root;
        }
    }
}
