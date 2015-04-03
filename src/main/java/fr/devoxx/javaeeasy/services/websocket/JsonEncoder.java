package fr.devoxx.javaeeasy.services.websocket;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import javax.json.Json;
import javax.json.stream.JsonGeneratorFactory;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class JsonEncoder implements Encoder.TextStream<SlotAttendees> {
    private JsonGeneratorFactory factory;

    @Override
    public void init(final EndpointConfig endpointConfig) {
        factory = Json.createGeneratorFactory(Collections.<String, Object>emptyMap());
    }

    @Override
    public void encode(final SlotAttendees object, final Writer writer) throws EncodeException, IOException {
        factory.createGenerator(writer)
                    .writeStartObject()
                        .write("slotId", object.getSlotId())
                        .write("attendees", object.getAttendees())
                    .writeEnd()
                .close();
    }

    @Override
    public void destroy() {
        // no-op
    }
}
