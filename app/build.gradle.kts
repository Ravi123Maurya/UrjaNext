plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id ("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

    id("org.jetbrains.kotlin.plugin.compose")

}

android {
    namespace = "com.ravimaurya.urjanext"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ravimaurya.urjanext"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }


        buildConfigField("String", "MAPS_API_KEY", "\"${project.findProperty("MAPS_API_KEY")}\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "2.1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    secrets{
        // Configure the plugin
        defaultPropertiesFileName = "gradle.properties"
    }
}

dependencies {

    // navigation
    implementation("androidx.navigation:navigation-compose:2.8.9")

    // viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    // Material Icons
    implementation("androidx.compose.material:material-icons-extended-android:1.7.8")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // Firebase Bom
    // Firebase Authentication
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation("com.google.firebase:firebase-auth")

    // Hilt Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.hilt:hilt-navigation:1.2.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")

    // Datastore
    implementation("androidx.datastore:datastore-preferences:1.1.4")

    // Google Maps Compose library
    // Google Maps Compose utility library
    //Google Services & Maps
    val mapsComposeVersion = "4.3.3"
    implementation ("com.google.android.gms:play-services-maps:19.1.0")
    implementation ("com.google.android.gms:play-services-places:17.1.0")
    implementation("com.google.android.libraries.places:places:4.2.0")
    implementation("com.google.maps.android:maps-compose:$mapsComposeVersion")
    implementation ("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.maps.android:maps-compose-utils:$mapsComposeVersion")
    implementation("com.google.maps:google-maps-services:2.2.0")

    //Accompanist (Permission)
    implementation("com.google.accompanist:accompanist-permissions:0.33.1-alpha")

    // FireStore
    implementation("com.google.firebase:firebase-firestore-ktx")



    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    val camerax_version = "1.3.0-alpha04"
    implementation("androidx.camera:camera-core:$camerax_version")
    implementation("androidx.camera:camera-camera2:$camerax_version")
    implementation("androidx.camera:camera-lifecycle:$camerax_version")
    implementation("androidx.camera:camera-view:$camerax_version")

    //Barcode
    implementation("com.google.mlkit:barcode-scanning:17.3.0")

    implementation("com.google.guava:guava:33.3.0-android")

    //
    implementation("com.google.android.material:material:1.12.0")

}