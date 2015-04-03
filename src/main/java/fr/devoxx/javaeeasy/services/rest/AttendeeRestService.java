package fr.devoxx.javaeeasy.services.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;

@ApplicationScoped
@Path("attendee")
public class AttendeeRestService {


    @GET
    @Path("slots")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<String> getSlotIds() {
        return new ArrayList<>();
    }
}
