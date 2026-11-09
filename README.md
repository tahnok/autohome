<img src="assets/icon.svg" width="64" height="64" alt="">

# Autohome

An Android app that sends you to the home screen when you unlock your phone after the
screen has been off for a while, instead of dropping you back into the last app you used.
Pick the delay in the app (30 seconds to 1 hour, default 10 minutes).

It runs as an accessibility service: it listens for screen off/on and unlock, and when the
screen has been off long enough it presses Home. It does not read any screen content.

## Install

Download the APK from the [latest release](../../releases/latest) and open it on your phone. You will need to allow
installing apps from unknown sources.

## Build from source

```sh
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

Then open Autohome and tap **Open accessibility settings** to turn on Autohome.

On Android 13 and later, sideloaded apps can't turn on accessibility services until you allow it:
Settings → Apps → Autohome → ⋮ → **Allow restricted settings**. After that, turn the service on.

## CI and releases

`.github/workflows/build.yml` runs on every push and pull request. It runs the tests and lint, builds a debug APK, and
uploads the APK as a workflow artifact.

To publish a release, push a tag:

```sh
git tag v1.0.0
git push origin v1.0.0
```

This builds a minified release APK and attaches it to a GitHub release for the tag, with generated release notes.

### Release signing

Android only installs an update if it is signed with the same key as the installed version. Releases are signed with
the key from these repository secrets (Settings → Secrets and variables → Actions):

| Secret                    | Value                           |
|---------------------------|---------------------------------|
| `SIGNING_KEYSTORE_BASE64` | base64-encoded `release.jks`    |
| `SIGNING_STORE_PASSWORD`  | keystore password               |
| `SIGNING_KEY_ALIAS`       | key alias                       |
| `SIGNING_KEY_PASSWORD`    | key password                    |

Without them, release APKs are signed with a throwaway debug key.
