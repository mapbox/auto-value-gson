// modified by mapbox
package com.ryanharter.auto.value.gson.example;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.ryanharter.auto.value.gson.UnrecognisedJsonProperties;

import java.util.Map;

@AutoValue
public abstract class UnrecognisedExample {

  public abstract int knownField();

  public static UnrecognisedExample.Builder builder() {
    return new AutoValue_UnrecognisedExample.Builder();
  }

  public static TypeAdapter<UnrecognisedExample> typeAdapter(Gson gson) {
    return new AutoValue_UnrecognisedExample.GsonTypeAdapter(gson);
  }

  @Nullable
  @UnrecognisedJsonProperties
  abstract Map<String, Object> unknownProperties();

  @AutoValue.Builder
  public static abstract class Builder {
    public abstract Builder knownField(int value);
    abstract Builder unknownProperties(@Nullable Map<String, Object> value);
    public abstract UnrecognisedExample build();
  }

  public @interface Nullable {}
}