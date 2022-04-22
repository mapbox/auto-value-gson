// modified by mapbox
package com.mapbox.auto.value.gson;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Indicates that the annotated field is a container for unrecognised JSON properties.
 * The field has to be of type Map<String, SerializableJsonElement>.
 */
@Retention(SOURCE)
@Target(METHOD)
public @interface UnrecognizedJsonProperties {
}
