package fr.devoxx.javaeeasy.services.rest;

import fr.devoxx.javaeeasy.interceptor.Benchmark;
import fr.devoxx.javaeeasy.models.SlotsHolder;
import fr.devoxx.javaeeasy.models.cfp.Slot;
import fr.devoxx.javaeeasy.services.business.MessageBroadCaster;
import fr.devoxx.javaeeasy.services.business.SlotService;
import fr.devoxx.javaeeasy.services.jpa.SlotJpa;
import fr.devoxx.javaeeasy.services.websocket.SlotAttendees;

import java.util.Collection;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
@Path("conferences")
public class SlotRestService {
	@Inject
	private SlotsHolder conferences;

	@Inject
	private SlotService slotService;

	@Inject
	private MessageBroadCaster messageBroadCaster;

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

	@HEAD
	@Path("attendees/increment/{id}")
	public void iGoToConference(@PathParam("id") final String slotId) {
		broadcast(slotService.addAttendeeToSlot(slotId));
	}

	@HEAD
	@Path("attendees/decrement/{id}")
	public void iDontGoAnymoreToConference(@PathParam("id") final String slotId) {
		broadcast(slotService.removeAttendeeFromSlot(slotId));
	}

	private void broadcast(final SlotJpa slot) {
		final SlotAttendees up = new SlotAttendees();
		up.setAttendees(slotService.count(slot));
		up.setSlotId(slot.getId());
		messageBroadCaster.broadcast(up);
	}
}
