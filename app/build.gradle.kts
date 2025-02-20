plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.spotter20"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.spotter20"
        minSdk = 31
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.filament.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.play.services.maps.v1810)
    implementation (libs.play.services.location)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.glide)
    implementation (libs.androidx.recyclerview)
    implementation (libs.play.services.maps.v1810)
    implementation (libs.places)

    implementation (libs.play.services.location)
    implementation (libs.firebase.auth.ktx)


    implementation(libs.mpandroidchart)

//    implementation("com.github.PhilJay:MPAndroidChart:v3.0.3")

//    implementation(files("libs/MPAndroidChart.aar"))
//    implementation ("com.github.User:Repo:Tag")






}