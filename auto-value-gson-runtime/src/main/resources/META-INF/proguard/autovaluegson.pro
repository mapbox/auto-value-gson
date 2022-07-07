# modified by mapbox
# Annotations are for embedding static analysis information.
-dontwarn org.jetbrains.annotations.**
-dontwarn com.google.errorprone.annotations.**

-keepclassmembers class com.mapbox.auto.value.gson.SerializableJsonElement {
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
}
