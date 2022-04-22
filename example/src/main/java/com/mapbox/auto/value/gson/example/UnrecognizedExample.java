// modified by mapbox
package com.mapbox.auto.value.gson.example;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.mapbox.auto.value.gson.SerializableJsonElement;
import com.mapbox.auto.value.gson.UnrecognizedJsonProperties;

import java.util.Map;

@AutoValue
public abstract class UnrecognizedExample {

  public abstract int knownField();

  public static UnrecognizedExample.Builder builder() {
    return new AutoValue_UnrecognizedExample.Builder();
  }

  public static TypeAdapter<UnrecognizedExample> typeAdapter(Gson gson) {
    return new AutoValue_UnrecognizedExample.GsonTypeAdapter(gson);
  }

  @Nullable
  @UnrecognizedJsonProperties
  abstract Map<String, SerializableJsonElement> unknownProperties();

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder knownField(int value);
    abstract Builder unknownProperties(@Nullable Map<String, SerializableJsonElement> value);
    public abstract UnrecognizedExample build();
  }

  public @interface Nullable {}
}
