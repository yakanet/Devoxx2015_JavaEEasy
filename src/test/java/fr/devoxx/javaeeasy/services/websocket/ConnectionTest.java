package fr.devoxx.javaeeasy.services.websocket;

import fr.devoxx.javaeeasy.services.business.MessageBroadCaster;
import org.apache.johnzon.mapper.Mapper;
import org.apache.johnzon.mapper.MapperBuilder;
import org.apache.tomee.embedded.Configuration;
import org.apache.tomee.embedded.junit.TomEEEmbeddedRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import static java.util.Collections.singletonList;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static org.junit.Assert.assertEquals;

public class ConnectionTest {
    @Rule // this rule will start TomEE container and deploy the application from the classpath
    public final TomEEEmbeddedRule tomee = new TomEEEmbeddedRule(
            new Configuration()
                    .randomHttpPort() // don't use a fixed port to avoid issue on CI when multiple builds are concurrent
                    .user("devoxx", "devoxx") // we secured the application so ensure we have a user
                    // dynamically set the http port as system property
                    // it is actually a TomEE property but TomEE will set it back as system property so we are happy with it
                    .property("client.url", "http://localhost:${tomee.embedded.http}/rest/api/"), "")
            .injectOn(this);

    @Inject
    private MessageBroadCaster mgr;

    @Test
    public void checkUpdates() throws Exception {
        // get the WS container
        final WebSocketContainer ws = ContainerProvider.getWebSocketContainer();
        // dynamically build the server URI
        final URI uri = new URI("ws://localhost:" + tomee.getPort() + "/ws/planning");

        // don't forget server is secured to add the BASIC header (see web.xml)
        // and for easiness add a JsonDecoder to get object and not string/stream
        final ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create()
                .configurator(new ClientEndpointConfig.Configurator() {
                    @OnMessage
                    public void beforeRequest(final Map<String, List<String>> headers) {
                        headers.put("Authorization", singletonList("Basic " + printBase64Binary("devoxx:devoxx".getBytes())));
                    }
                })
                .decoders(singletonList(JsonDecoder.class))
                .build();

        // config is ready, just connect and wait for messages coming from the server
        // when something is broadcasted
        final Session s = ws.connectToServer(Client.class, clientConfig, uri);

        // keep trace of expected results (sent messages)
        final Collection<SlotAttendees> sentSlotAttendeeses = new ArrayList<>(2);
        {
            final SlotAttendees message = new SlotAttendees();
            message.setAttendees(150);
            message.setSlotId("Hands On Lab :: JavaEE is Easy");
            mgr.broadcast(message);
            sentSlotAttendeeses.add(message);
        }
        {
            final SlotAttendees message = new SlotAttendees();
            message.setAttendees(1000);
            message.setSlotId("Apache TomEE");
            mgr.broadcast(message);
            sentSlotAttendeeses.add(message);
        }

        // wait the client got messages - don't wait indefinitely to not hang on this test if something is wrong
        Client.LATCH.await(1, TimeUnit.MINUTES);
        // close properly the connection
        s.close(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, "bye!"));

        // check we got expected messages
        assertEquals(sentSlotAttendeeses.size(), Client.SLOT_ATTENDEES.size());
        final Iterator<SlotAttendees> expected = sentSlotAttendeeses.iterator();
        final Iterator<SlotAttendees> actual = Client.SLOT_ATTENDEES.iterator();
        while (expected.hasNext()) {
            final SlotAttendees ex = expected.next();
            final SlotAttendees ac = actual.next();
            assertEquals(ex.getAttendees(), ac.getAttendees());
            assertEquals(ex.getSlotId(), ac.getSlotId());
        }
    }

    public static class Client extends Endpoint {
        public static final List<SlotAttendees> SLOT_ATTENDEES = new LinkedList<>();
        public static final CountDownLatch LATCH = new CountDownLatch(2);

        @Override
        public void onOpen(final Session session, final EndpointConfig config) {
            // just add a listener which will stack the messages and release the waiting thread once 2 messages has been read
            session.addMessageHandler(new MessageHandler.Whole<SlotAttendees>() {
                @Override
                public void onMessage(final SlotAttendees message) {
                    SLOT_ATTENDEES.add(message);
                    LATCH.countDown();
                }
            });
        }
    }

    public static class JsonDecoder implements Decoder.TextStream<SlotAttendees> {
        private Mapper mapper;

        @Override
        public void init(final EndpointConfig endpointConfig) {
            mapper = new MapperBuilder().build();
        }

        @Override
        public SlotAttendees decode(final Reader reader) throws DecodeException, IOException {
            return mapper.readObject(reader, SlotAttendees.class);
        }

        @Override
        public void destroy() {
            // no-op
        }
    }
}
