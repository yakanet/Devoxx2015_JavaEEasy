package fr.devoxx.javaeeasy.mock;

import fr.devoxx.javaeeasy.models.cfp.Slot;

import org.apache.openejb.loader.IO;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Path("api/conferences/DevoxxFR2015/")
public class DevoxxServerMock {
    @GET
    @Path("schedules")
    public Response links(@Context final UriInfo uri) {
        try {
            String json = IO.slurp(getClass().getResource("schedulesResponse_01.json"));
            json=json.replaceAll("http://cfp.devoxx.fr/api/conferences/DevoxxFR2015/schedules",uri.getRequestUriBuilder().build().toASCIIString());
            return Response.ok(json, MediaType.APPLICATION_JSON_TYPE).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    @GET
    @Path("schedules/{day}")
    public Response conferences(@PathParam("day") final String day) {
        try {
            System.out.println("schedulesResponse_" + day + "_01.json");
            String json = IO.slurp(getClass().getResource("schedulesResponse_" + day + "_01.json"));
            return Response.ok(json, MediaType.APPLICATION_JSON_TYPE).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.serverError().build();
        }
    }

    public static void assertConferences(final Collection<Slot> conferences) {
        assertNotNull(conferences);
        assertEquals(233, conferences.size());
        List<Slot> allConferencesList = new LinkedList<>();
        allConferencesList.addAll(conferences);

        assertEquals("monday",allConferencesList.get(0).getDay());
        assertEquals("a_hall",allConferencesList.get(0).getRoomId());
        assertEquals("monday",allConferencesList.get(1).getDay());
        assertEquals("room4",allConferencesList.get(1).getRoomId());
        assertEquals("friday",allConferencesList.get(232).getDay());
        assertEquals("room9",allConferencesList.get(232).getRoomId());

    }
}
