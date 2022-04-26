// modified by mapbox
package com.mapbox.auto.value.gson.example;

import com.google.gson.TypeAdapterFactory;
import com.mapbox.auto.value.gson.GsonTypeAdapterFactory;

@GsonTypeAdapterFactory
public abstract class SampleAdapterFactory implements TypeAdapterFactory {

    public static SampleAdapterFactory create() {
        return new AutoValueGson_SampleAdapterFactory();
    }
}
