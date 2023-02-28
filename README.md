# Android-Wear-DataAPI
#LyaWear

The LyaWear project describes the connection between the mobile app and the wear app.

The purpose of this implementation is to optimize the media volume in context aware, using the location to detect the user's current location and the volume level detected in that context, then after learning it is possible to automatically change the volume to preference in that location.

* Wear App compatible with Android
* Pass data from mobile app to Wear app using DataAPI and GoogleApiClient
* Using 'WearableListenerService' retrieve the data from the end
* Kotlin and java support

# Observation

- Add Google Play service dependencies in both build.gradle files
- Package name for mobile and wear apps must be the same
- Add gsm version code in both manifest files
- Add hostName, path prefix and scheme for connection in manifest file
- Must use same signatures for both (if any keystore file is used in mobile app to generate apk files then must use same keystore file for wear app)
