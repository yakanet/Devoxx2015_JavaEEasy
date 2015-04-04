package fr.devoxx.javaeeasy.services.rest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import fr.devoxx.javaeeasy.interceptor.Benchmark;
import fr.devoxx.javaeeasy.models.SlotsHolder;
import fr.devoxx.javaeeasy.models.cfp.Slot;

@ApplicationScoped
@Path("conferences")
public class SlotRestService {

	@Inject
	private SlotsHolder conferences;


	@Inject
	private Logger LOG;

	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Benchmark
	public Collection<Slot> getConferences(){
		LOG.info("call to SlotRestService.getConferences");
		return conferences.getConferences();
	}

}
