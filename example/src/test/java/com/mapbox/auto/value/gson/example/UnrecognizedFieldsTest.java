// modified by mapbox
package com.mapbox.auto.value.gson.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mapbox.auto.value.gson.SerializableJsonElement;
import org.junit.Test;

import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;

public class UnrecognizedFieldsTest {

    @Test
    public void readWriteFullyRecognisedJson() {
        Gson gson = createGson();
        String sourceJson = "{\"knownField\":9}";

        UnrecognizedExample object = gson.fromJson(sourceJson, UnrecognizedExample.class);
        String json = gson.toJson(object);

        assertEquals(sourceJson, json);
    }

    @Test
    public void readWriteTwoUnknownFields() {
        Gson gson = createGson();
        String sourceJson = "{\"knownField\":9,\"unknownField\":7,\"oneMoreUnknown\":true}";

        UnrecognizedExample object = gson.fromJson(sourceJson, UnrecognizedExample.class);
        String json = gson.toJson(object);

        assertEquals(sourceJson, json);
    }

    @Test
    public void readAndWriteUnknownObject() {
        Gson gson = createGson();
        String originalJson = "{\"knownField\":9,\"unknownObject\":{\"unknownField\":\"test\"}}";

        UnrecognizedExample object = gson.fromJson(originalJson, UnrecognizedExample.class);
        String json = gson.toJson(object);

        assertEquals(originalJson, json);
    }

    @Test
    public void readAndWriteUnknownArray() {
        Gson gson = createGson();
        String originalJson = "{\"knownField\":9,\"unknownArray\":[1,2,true,{\"a\":\"b\"}]}";

        UnrecognizedExample object = gson.fromJson(originalJson, UnrecognizedExample.class);
        String json = gson.toJson(object);

        assertEquals(originalJson, json);
    }

    @Test
    public void toJsonFromJson() {
        Gson gson = createGson();
        UnrecognizedExample objectFromBuilder = UnrecognizedExample.builder().knownField(4).build();

        String json = gson.toJson(objectFromBuilder);
        UnrecognizedExample objectFromJson = gson.fromJson(json, UnrecognizedExample.class);

        assertEquals(objectFromBuilder, objectFromJson);
    }

    @Test
    public void toJsonFromJsonWithUnrecognised() {
        Gson gson = createGson();
        UnrecognizedExample objectFromBuilder = UnrecognizedExample.builder()
          .knownField(4)
          .unknownProperties(new LinkedHashMap<String, SerializableJsonElement>(){{
              put("testInt", new SerializableJsonElement(new JsonPrimitive(1)));
              put("testDouble", new SerializableJsonElement(new JsonPrimitive(12.4d)));
              JsonArray testArray = new JsonArray();
              testArray.add(true);
              testArray.add("test4");
              testArray.add(4);
              testArray.add(4.9d);
              put("testArray", new SerializableJsonElement(testArray));
              JsonObject testObject = new JsonObject();
              testObject.add("testString", new JsonPrimitive("88"));
              put("testObject", new SerializableJsonElement(testObject));
          }})
          .build();

        String json = gson.toJson(objectFromBuilder);
        UnrecognizedExample objectFromJson = gson.fromJson(json, UnrecognizedExample.class);

        assertEquals(objectFromBuilder, objectFromJson);
    }

    private Gson createGson() {
        return new GsonBuilder()
          .registerTypeAdapterFactory(SampleAdapterFactory.create())
          .create();
    }
}
