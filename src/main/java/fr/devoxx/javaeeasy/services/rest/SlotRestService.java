package fr.devoxx.javaeeasy.services.rest;

import fr.devoxx.javaeeasy.interceptor.Benchmark;
import fr.devoxx.javaeeasy.models.SlotsHolder;
import fr.devoxx.javaeeasy.models.cfp.Slot;
import fr.devoxx.javaeeasy.services.business.SlotService;

import java.util.Collection;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("conferences")
public class SlotRestService {
	@Inject
	private SlotsHolder conferences;

	@Inject
	private SlotService slotService;

	@GET
	@Benchmark
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Slot> getConferences(){
		return conferences.getConferences();
	}

	@GET
	@Path("attendees")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String,Integer> getAttendeesBySlotId(){
		return slotService.getAttendeesCountGroupedBySlotId();
	}
}
