plugins {
    id("com.android.application")
}

android {
    namespace = "dev.autohome"
    compileSdk = 37

    defaultConfig {
        applicationId = "dev.autohome"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
            // Signed with the debug key so `assembleRelease` produces an installable APK.
            signingConfig = signingConfigs.getByName("debug")
        }
    }
}
