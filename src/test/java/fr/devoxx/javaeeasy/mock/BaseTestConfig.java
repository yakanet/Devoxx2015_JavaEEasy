package fr.devoxx.javaeeasy.mock;

import java.net.URL;

import javax.inject.Inject;

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
    // ContainerRule defines an ApplicationComposer container aiming at host apps
    protected final ContainerRule container = new ContainerRule(new Container());
    // Application rule define an EE app to test
    protected final ApplicationRule app = new ApplicationRule(new JavaEEEasy());

    @Rule
    public final TestRule rule = RuleChain
            .outerRule(container)
            .around(new ApplicationRule(new DevoxxApp())) // deploy it first since we suppose it is present in our app {@see ConferenceRetriever}
            .around(app);

    // this is Mock app that emulate the devoxx cfp api, provide some static JSON contents on REST requests
    @Classes(context = "devoxx", value = DevoxxServerMock.class)
    public static class DevoxxApp {
    }

    // this is part of the JavaEEasy app we want to test
    @Default // This get all classes from current module
    @Classes(context = "app", cdi = true, excludes = "fr.devoxx.javaeeasy.mock") //excepted that
    public static class JavaEEEasy {
        @Inject
        private DevoxxClient client;

        public DevoxxClient getClient() {
            return client;
        }
    }

    // this define the "container" to host apps, the mock one and the JavaEEasy one
    @EnableServices("jaxrs") // activate all jaxrs stack
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
