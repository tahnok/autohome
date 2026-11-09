plugins {
    id("com.android.application")
}

// Release signing is configured through environment variables so the keystore never lives in
// the repository. CI decodes the keystore from a secret; see README.md.
val releaseKeystore: String? = System.getenv("SIGNING_KEYSTORE_FILE")

android {
    namespace = "dev.autohome"
    compileSdk = 37

    defaultConfig {
        applicationId = "io.github.tahnok.autohome"
        minSdk = 26
        targetSdk = 37
        versionCode = (System.getenv("VERSION_CODE") ?: "1").toInt()
        versionName = System.getenv("VERSION_NAME") ?: "0.1.0-dev"
    }

    signingConfigs {
        if (releaseKeystore != null) {
            create("release") {
                storeFile = file(releaseKeystore)
                storePassword = System.getenv("SIGNING_STORE_PASSWORD")
                keyAlias = System.getenv("SIGNING_KEY_ALIAS")
                keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            // Without a release keystore, fall back to the debug key so the APK is still installable.
            signingConfig = signingConfigs.findByName("release") ?: signingConfigs.getByName("debug")
        }
    }
}
