// modified by mapbox
package com.ryanharter.auto.value.gson.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryanharter.auto.value.gson.UnrecognizedJsonPropertiesFeatureFlag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UnrecognizedFieldsTest {

    @Before
    public void turnOnFeature() {
        UnrecognizedJsonPropertiesFeatureFlag.enable();
    }

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
    public void readAndWriteUnknownArrayWhenFeatureIsTurnedOff() {
        UnrecognizedJsonPropertiesFeatureFlag.disable();
        Gson gson = createGson();
        String originalJson = "{\"knownField\":9,\"unknownArray\":[1,2,true,{\"a\":\"b\"}]}";

        UnrecognizedExample object = gson.fromJson(originalJson, UnrecognizedExample.class);
        String json = gson.toJson(object);

        String expectedJson = "{\"knownField\":9}";
        assertEquals(expectedJson, json);
    }

    @Test
    public void readAndWriteUnknownArrayWhenFeatureTurnedOffInTheMiddle() {
        Gson gson = createGson();
        String originalJson = "{\"knownField\":9,\"unknownArray\":[1,2,true,{\"a\":\"b\"}]}";

        UnrecognizedExample object = gson.fromJson(originalJson, UnrecognizedExample.class);
        UnrecognizedJsonPropertiesFeatureFlag.disable();
        String json = gson.toJson(object);

        String expectedJson = "{\"knownField\":9}";
        assertEquals(expectedJson, json);
    }

    private Gson createGson() {
        return new GsonBuilder()
          .registerTypeAdapterFactory(SampleAdapterFactory.create())
          .create();
    }
}
