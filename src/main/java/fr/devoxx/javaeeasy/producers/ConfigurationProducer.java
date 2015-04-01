package fr.devoxx.javaeeasy.producers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@ApplicationScoped
public class ConfigurationProducer {
    @Produces
    @Configuration("")
    public String property(final InjectionPoint ip) {
        final Configuration configuration = ip.getAnnotated().getAnnotation(Configuration.class);
        return System.getProperty(configuration.value(), configuration.otherwise());
    }

    @Produces
    @Configuration("")
    public Class propertyAsClass(final InjectionPoint ip) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(property(ip));
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Produces
    @Configuration("")
    public Integer integer(final InjectionPoint ip) {
        return Integer.parseInt(property(ip));
    }
}
