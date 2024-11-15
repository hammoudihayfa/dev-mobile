plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.bookstore"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bookstore"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Room dependencies for local database
    implementation("androidx.room:room-runtime:2.5.0")
    annotationProcessor("androidx.room:room-compiler:2.5.0") // Using annotationProcessor instead of kapt

    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // AndroidX dependencies
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")
    // Material Design for UI components
    implementation("com.google.android.material:material:1.9.0")
    // JSON handling with Gson
    implementation("com.google.code.gson:gson:2.8.8")
    implementation("com.sun.mail:javax.mail:1.6.2") // JavaMail API
    implementation("javax.activation:activation:1.1.1") // Java Activation Framework




} 