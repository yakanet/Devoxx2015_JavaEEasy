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

import org.apache.johnzon.jaxrs.JohnzonProvider;

import fr.devoxx.javaeeasy.models.cfp.Link;
import fr.devoxx.javaeeasy.models.cfp.LinksSchema;
import fr.devoxx.javaeeasy.models.cfp.Slot;
import fr.devoxx.javaeeasy.models.cfp.SlotsSchema;

@ApplicationScoped
public class DevoxxClient {

    private String base="http://cfp.devoxx.fr/api/";

    private String conference = "DevoxxFR2015";

    private Class jsonProvider =JohnzonProvider.class;

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
