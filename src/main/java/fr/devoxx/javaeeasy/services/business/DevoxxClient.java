package fr.devoxx.javaeeasy.services.business;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import fr.devoxx.javaeeasy.models.cfp.Link;
import fr.devoxx.javaeeasy.models.cfp.LinksSchema;
import fr.devoxx.javaeeasy.models.cfp.Slot;
import fr.devoxx.javaeeasy.models.cfp.SlotsSchema;


public class DevoxxClient {

    public List<Slot> retrieveSlots() {
        return null;
    }

}
