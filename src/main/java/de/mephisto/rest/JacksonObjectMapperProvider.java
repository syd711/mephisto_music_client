package de.mephisto.rest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Provider implementation for Jersey, we use Jackson here.
 */
@Provider
public class JacksonObjectMapperProvider implements ContextResolver<ObjectMapper> {

  ObjectMapper defaultObjectMapper;

  public JacksonObjectMapperProvider() {
    defaultObjectMapper = createDefaultMapper();
  }

  @Override
  public ObjectMapper getContext(Class<?> type) {
    return defaultObjectMapper;
  }

  private static ObjectMapper createDefaultMapper() {
    final ObjectMapper result = new ObjectMapper();
    result.configure(Feature.INDENT_OUTPUT, true);
    return result;
  }
}