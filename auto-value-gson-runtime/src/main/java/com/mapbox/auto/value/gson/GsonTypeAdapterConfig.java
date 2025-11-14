package com.mapbox.auto.value.gson;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Set of configuration properties for the generated GsonTypeAdapter.
 */
@Retention(SOURCE)
@Target(TYPE)
public @interface GsonTypeAdapterConfig {

  boolean USE_BUILDER_ON_READ_DEFAULT = true;

  /**
   * If true and a builder method is present then a Builder object will be used when reading.
   * If false the object will be constructed directly, no builder will be instantiated.
   * @return true if use builder when reading, false if directly use object constructor.
   */
  boolean useBuilderOnRead() default USE_BUILDER_ON_READ_DEFAULT;
}
