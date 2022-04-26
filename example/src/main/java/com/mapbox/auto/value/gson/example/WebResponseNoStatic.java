// modified by mapbox
package com.mapbox.auto.value.gson.example;

import com.google.auto.value.AutoValue;
import com.mapbox.auto.value.gson.GenerateTypeAdapter;

import java.util.List;
import java.util.Map;

@AutoValue
@GenerateTypeAdapter
public abstract class WebResponseNoStatic<T> {
  public abstract int status();
  public abstract T data();
  public abstract List<T> dataList();
  public abstract Map<String, List<T>> dataMap();
}
