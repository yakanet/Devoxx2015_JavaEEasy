package fr.devoxx.javaeeasy.services.rest;

import org.apache.johnzon.jaxrs.DelegateProvider;
import org.apache.johnzon.jaxrs.JohnzonMessageBodyReader;
import org.apache.johnzon.jaxrs.Jsons;
import org.apache.johnzon.mapper.Mapper;
import org.apache.johnzon.mapper.MapperBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Provider // workaround for windows encoding issue, fixed on johnzon snapshot
public class UTF8JsonProvider<T> extends DelegateProvider<T> {
    private static final Mapper MAPPER = new MapperBuilder().build();

    protected UTF8JsonProvider() {
        super(
            new JohnzonMessageBodyReader<>(MAPPER, null),
            new MessageBodyWriter<T>() {
                @Override
                public long getSize(final T t, final Class<?> rawType, final Type genericType,
                                    final Annotation[] annotations, final MediaType mediaType) {
                    return -1;
                }

                @Override
                public boolean isWriteable(final Class<?> rawType, final Type genericType,
                                           final Annotation[] annotations, final MediaType mediaType) {
                    return Jsons.isJson(mediaType)
                            && StreamingOutput.class != rawType
                            && String.class != rawType
                            && Response.class != rawType;
                }

                @Override
                public void writeTo(final T t, final Class<?> rawType, final Type genericType,
                                    final Annotation[] annotations, final MediaType mediaType,
                                    final MultivaluedMap<String, Object> httpHeaders,
                                    final OutputStream entityStream) throws IOException {
                    if (rawType.isArray()) {
                        MAPPER.writeArray(t, entityStream);
                    } else if (Collection.class.isAssignableFrom(rawType) && ParameterizedType.class.isInstance(genericType)) {
                        MAPPER.writeArray(Collection.class.cast(t), new OutputStreamWriter(entityStream, "UTF-8"));
                    } else {
                        MAPPER.writeObject(t, entityStream);
                    }
                }
            });
    }
}
