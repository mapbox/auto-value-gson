[comment]: <> modified by Mapbox

Releasing
========

1. Change the version in `gradle.properties` to a non-SNAPSHOT version.
2. `./gradlew clean build`
3. `./gradlew mapboxSDKRegistryUpload`