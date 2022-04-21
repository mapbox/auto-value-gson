// modified by mapbox
package com.ryanharter.auto.value.gson;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;

/***
 * Wraps {@link JsonElement} and handles java serialization.
 */
public class SerializableJsonElement implements Serializable {

  private JsonElement element;

  public SerializableJsonElement(JsonElement element) {
    this.element = element;
  }

  public JsonElement getElement() {
    return this.element;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SerializableJsonElement that = (SerializableJsonElement) o;
    return Objects.equals(element, that.element);
  }

  @Override public int hashCode() {
    return Objects.hash(element);
  }

  private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
  {
    String json = aInputStream.readUTF();
    element = new GsonBuilder().create().fromJson(json, JsonElement.class);
  }

  private void writeObject(ObjectOutputStream aOutputStream) throws IOException
  {
    String json = element.toString();
    aOutputStream.writeUTF(json);
  }
}
