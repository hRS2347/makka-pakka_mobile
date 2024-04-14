plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
//    id("com.chaquo.python")
    id("kotlin-kapt")
//    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.makka_pakka"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.makka_pakka"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}
//chaquopy {
//    defaultConfig {
//        pip {
//            install("torch")
//            install("numpy")
//            install("scipy")
//            install("matplotlib")
//            install("wheel")
//        }
//    }
//}
dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.squareup.okhttp3:okhttp:4.11.0")
//    implementation("org.greenrobot:eventbus:3.2.0")
    //gson
    implementation("com.google.code.gson:gson:2.10.1")
    //markwon
    implementation("io.noties.markwon:core:4.6.2")

//    implementation("androidx.room:room-runtime:2.6.1")
//    annotationProcessor("androidx.room:room-compiler:2.6.1")
//    implementation("androidx.room:room-ktx:2.6.1")
//    kapt("androidx.room:room-compiler:2.6.1")

    implementation("com.github.tbruyelle:rxpermissions:0.12")
    implementation("io.reactivex.rxjava3:rxjava:3.1.6")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
//    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("javax.activation:activation:1.1.1")

    // Preferences DataStore (SharedPreferences like APIs)
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-preferences-core:1.0.0")

    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    implementation("androidx.work:work-runtime-ktx:2.9.0")

//    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
//    implementation ("org.nanohttpd:nanohttpd:2.3.1")
    //rxpermissions
    api ("com.tencent.tbs:tbssdk:44286")

    implementation ("com.github.crazyandcoder:citypicker:6.0.2")
//    implementation ("com.github.ertugrulkaragoz:SuperBottomBar:0.4")
    implementation ("com.github.iammert:ReadableBottomBar:0.2")
}