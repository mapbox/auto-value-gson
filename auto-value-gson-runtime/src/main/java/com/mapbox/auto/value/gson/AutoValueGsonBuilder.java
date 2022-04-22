// modified by mapbox
package com.mapbox.auto.value.gson;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * If present, indicates that the annotated method should be used for retrieving an instance of the
 * AutoValue.Builder. Only necessary if there is more than one builder method.
 */
@Retention(CLASS)
@Target(METHOD)
public @interface AutoValueGsonBuilder {
}
