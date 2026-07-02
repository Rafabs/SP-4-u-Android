plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android") // Removido o 'apply false' daqui
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.rafabs.sp4u"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.rafabs.sp4u"
        minSdk = 26
        targetSdk = 36
        versionCode = 3
        versionName = "26.07.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    configurations.all {
        exclude(group = "com.intellij", module = "annotations")
    }
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.14.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.8")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.8")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.11.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.4.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.4.0")
    implementation("org.osmdroid:osmdroid-android:6.1.20")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("com.airbnb.android:lottie:6.7.1")

    // Room
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.11.2")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}