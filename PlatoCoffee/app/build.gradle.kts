plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android") // Add Kotlin plugin
    id("kotlin-parcelize") // For parcelable objects
}

android {
    namespace = "com.example.platocoffee"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.platocoffee"
        minSdk = 25
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        viewBinding = true // Enable view binding
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0") // Kotlin extensions
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0") // ViewModel
    implementation("androidx.activity:activity-ktx:1.9.0") // Activity extensions

    // Firebase UI Auth for GitHub SSO
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("androidx.browser:browser:1.4.0") // Required for Firebase UI Auth

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.8.0")) // Use BOM for consistent versions
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore") // For future database use
    implementation("com.google.firebase:firebase-messaging") // For push notifications

    // Coroutines for async operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}