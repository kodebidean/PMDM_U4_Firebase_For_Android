plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.kodeleku.pmdm_u4_firebase"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kodeleku.pmdm_u4_firebase"
        minSdk = 31
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding{
        enable=true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // FIREBASE BoM
    implementation (platform(libs.firebase.bom))
    // Firebase RealTime Database
    implementation(libs.firebase.database.ktx)
    // Firebase Cloud Storage
    implementation(libs.firebase.storage.ktx)
    // Firebase Authentication
    implementation(libs.firebase.auth.ktx)


    // COROUTINES
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.firebase.storage)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}