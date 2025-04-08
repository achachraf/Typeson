package io.github.achachraf.typeson.interlay;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.github.achachraf.typeson.TypesonException;
import io.github.achachraf.typeson.interlay.mock.Circle;
import io.github.achachraf.typeson.interlay.mock.Figure;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static com.fasterxml.jackson.core.json.JsonReadFeature.ALLOW_SINGLE_QUOTES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TypesonObjectMapperConfigTest {

   @Test
   void testSerializationDefault() {
      Typeson typeson = new Typeson();
      String json = typeson.marshall(new Figure());
      assertEquals("{\"name\":null,\"shapes\":[]}", json);
   }

   @Test
   void testSerializationOption() {
      ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);
      Typeson typeson = new Typeson(objectMapper);
      String json = typeson.marshall(new Figure());
      assertEquals("{\"shapes\":[]}", json);
   }

   @Test
   void testDeserializationDefault() {
      Typeson typeson = new Typeson();
      String json = "{'name':'my figure','shapes':[]}";
      Throwable throwable = assertThrows(TypesonException.class, () -> typeson.unmarshall(json, Figure.class));
      Throwable originalCause = throwable.getCause().getCause();
      assertEquals(JsonParseException.class, originalCause.getClass());
      assertThat(originalCause.getMessage(), Matchers.startsWith("Unexpected character (''' (code 39))"));
   }

   @Test
   void testDeserializationOption() {
      ObjectMapper objectMapper = JsonMapper.builder().enable(ALLOW_SINGLE_QUOTES).build();
      Typeson typeson = new Typeson(objectMapper);
      String json = "{'name':'my figure','shapes':[]}";
      Figure figure = typeson.unmarshall(json, Figure.class);
      assertEquals("my figure", figure.getName());
   }

   @Test
   void testFailsOnUnkownPropertiesEnabled() {
      //Enabled is the default state for this feature
      Typeson typeson = new Typeson();
      String json = "{\"name\":\"my figure\",\"size\":30,\"shapes\":[]}";
      Throwable throwable = assertThrows(TypesonException.class, () -> typeson.unmarshall(json, Figure.class));
   }

   @Test
   void testFailsOnUnknownPropertiesDisabled() {
      ObjectMapper objectMapper = JsonMapper.builder().disable(FAIL_ON_UNKNOWN_PROPERTIES).build();
      Typeson typeson = new Typeson(objectMapper);
      String json = "{\"name\":\"my figure\",\"size\":30,\"shapes\":[]}";
      assertDoesNotThrow(() -> typeson.unmarshall(json, Figure.class));
   }

   @Test
   void testFailsOnNullForPrimitiveDisabled() {
      //Disabled is the default state for this feature
      Typeson typeson = new Typeson();
      String json = "{}";
      assertDoesNotThrow(() -> typeson.unmarshall(json, Circle.class));
   }

   @Test
   void testFailsOnNullForPrimitiveEnabled() {
      ObjectMapper objectMapper = JsonMapper.builder().enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES).build();
      Typeson typeson = new Typeson(objectMapper);
      String json = "{}";
      assertThrows(TypesonException.class, () -> typeson.unmarshall(json, Circle.class));
   }

}
