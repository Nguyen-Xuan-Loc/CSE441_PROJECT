plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.cse441_project"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cse441_project"
        minSdk = 24
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
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Firebase Realtime Database
    implementation (libs.firebase.database.v2010)

    // Firebase Storage
    implementation (libs.firebase.storage)

    // Firebase Core (nếu chưa có)
    implementation (libs.firebase.core)
    implementation (libs.picasso)
//    implementation ("com.squareup.picasso:picasso:2.71828")
//    implementation ("com.squareup.picasso:picasso:2.71828")
//    implementation ("com.android.volley:volley:1.2.1")
//    implementation ("androidx.cardview:cardview:1.0.0")
//    implementation ("com.androidx.support:design:28.0.0")
//    implementation ("com.google.android.gms:play-services-maps:18.0.2")
//    implementation ("androidx.recyclerview:recyclerview:1.3.0-alpha02")
//    implementation ("com.androidx.support:recyclerview-v7:28.0.0")
}