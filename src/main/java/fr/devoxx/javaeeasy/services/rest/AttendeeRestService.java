package fr.devoxx.javaeeasy.services.rest;

import fr.devoxx.javaeeasy.services.business.SlotService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.Collection;

@ApplicationScoped
@Path("attendee")
public class AttendeeRestService {

    @Inject
    private SlotService slots;

    @Inject
    private Principal principal;

    @GET
    @Path("slots")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> getSlotIds(){
        return slots.getAllocatedSlotIdsForAttendee(principal.getName());
    }
}
