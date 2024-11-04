plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
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
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")

    // Firebase
    implementation(libs.firebase.database)

    // JSON parsing
    implementation("com.google.code.gson:gson:2.8.8")

    // Image loading
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation(libs.firebase.storage)
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    // Testing
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    // Firebase Realtime Database
    implementation(libs.firebase.database)



    implementation ("com.squareup.picasso:picasso:2.8")// Thêm dòng này


    // Firebase Storage
    implementation(libs.firebase.storage)

}