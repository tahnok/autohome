<img src="assets/icon.svg" width="64" height="64" alt="">

# Autohome

An Android app that sends you to the home screen when you unlock your phone after the
screen has been off for a while, instead of dropping you back into the last app you used.
Pick the delay in the app (30 seconds to 1 hour, default 10 minutes).

It runs as an accessibility service: it listens for screen off/on and unlock, and when the
screen has been off long enough it presses Home. It does not read any screen content.

## Build & install

```sh
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

Then open Autohome and tap **Open accessibility settings** to turn on Autohome.

On Android 13 and later, sideloaded apps can't turn on accessibility services until you allow it:
Settings → Apps → Autohome → ⋮ → **Allow restricted settings**. After that, turn the service on.
