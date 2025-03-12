[comment]: <> modified by Mapbox

Releasing
========

1. Change the version in `gradle.properties` to a non-SNAPSHOT version.
2. `./gradlew clean`
3. `./gradlew build`
4`./gradlew mapboxSDKRegistryUpload`