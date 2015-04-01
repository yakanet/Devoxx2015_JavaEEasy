package fr.devoxx.javaeeasy.services.rest;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.devoxx.javaeeasy.models.cfp.Slot;
import fr.devoxx.javaeeasy.services.business.DevoxxClient;

@ApplicationScoped
@Path("conferences")
public class SlotRestService {

    @Inject
    private DevoxxClient devoxxClient;

	@GET
    @Produces(MediaType.APPLICATION_JSON)
	public Collection<Slot> getConferences(){
		return devoxxClient.retrieveSlots();
	}

}
