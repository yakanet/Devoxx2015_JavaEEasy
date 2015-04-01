package fr.devoxx.javaeeasy.services.business;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import fr.devoxx.javaeeasy.models.cfp.Link;
import fr.devoxx.javaeeasy.models.cfp.LinksSchema;
import fr.devoxx.javaeeasy.models.cfp.Slot;
import fr.devoxx.javaeeasy.models.cfp.SlotsSchema;
import fr.devoxx.javaeeasy.producers.Configuration;

@ApplicationScoped
public class DevoxxClient {


    @Inject
    @Configuration(value = "client.url", otherwise = "http://cfp.devoxx.fr/api/")
    private String base;

    @Inject
    @Configuration(value = "client.conference", otherwise = "DevoxxFR2015")
    private String conference;

    @Inject
    @Configuration(value = "client.provider", otherwise = "org.apache.johnzon.jaxrs.JohnzonProvider")
    private Class jsonProvider;

    private Client restClient;
    private WebTarget linksWebTarget;

    @PostConstruct
    private void init() {
        restClient = ClientBuilder.newBuilder().build().register(jsonProvider);
        linksWebTarget = restClient.target(base).path("conferences/" + conference + "/schedules");
    }

    public List<Slot> retrieveSlots() {
        return linksWebTarget.request(MediaType.APPLICATION_JSON_TYPE)
                .get(LinksSchema.class)
                .getLinks()
                .stream()
                .map(this::retrieveLink)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<Slot> retrieveLink(final Link link) {
        return restClient.target(link.getHref())
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(SlotsSchema.class)
                .getSlots();
    }
}
