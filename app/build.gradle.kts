import java.util.Date
import java.text.SimpleDateFormat

fun getTimestampVersionCode(): Int {
    return SimpleDateFormat("yyMMddHH").format(Date()).toInt()
}

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.yzd.dztrain"
    compileSdk {
        version = release(36)
    }

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.yzd.dztrain"
        minSdk = 24
        targetSdk = 36
        versionCode = getTimestampVersionCode()
        versionName = "2.1.1"

        val currentDateTime = SimpleDateFormat("MMM dd, yyyy HH:mm").format(Date())
        buildConfigField("String", "BUILD_DATE", "\"$currentDateTime\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.core:core-splashscreen:1.2.0")
    implementation("com.google.code.gson:gson:2.13.2")
}