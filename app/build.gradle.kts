plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.dam2_e2_t6_mpgm"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.dam2_e2_t6_mpgm"
        minSdk = 28
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("org.parceler:parceler-api:1.1.13")
    annotationProcessor("org.parceler:parceler:1.1.13")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
}