plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.mehmetpetek.themoviedb"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mehmetpetek.themoviedb"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    productFlavors {
        create("google") {
            dimension = "platform"
        }
        create("live") {
            dimension = "env"
            applicationId = "com.mehmetpetek.themoviedb"
            resValue("string", "app_name", "The Movie DB")
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField(
                "String",
                "API_TOKEN",
                "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkMzAwOTM0MDhlNDE0ZGI5ODJmNTJlMDU0ZjY2N2Q5NyIsInN1YiI6IjYwMzk1MmZiNmQ5N2U2MDA0NTE5ZTYwOSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.d5C-08afDBaOLvQeVAnVu0YgtHvaC-S232PgAlrcxKE\""
            )
            buildConfigField("String", "IMAGE_BASE_URL", "\"https://image.tmdb.org/t/p/w500\"")
        }
        create("dev") {
            dimension = "env"
            applicationId = "com.mehmetpetek.themoviedb.beta"
            resValue("string", "app_name", "The Movie DB - BETA")
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField(
                "String",
                "API_TOKEN",
                "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkMzAwOTM0MDhlNDE0ZGI5ODJmNTJlMDU0ZjY2N2Q5NyIsInN1YiI6IjYwMzk1MmZiNmQ5N2U2MDA0NTE5ZTYwOSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.d5C-08afDBaOLvQeVAnVu0YgtHvaC-S232PgAlrcxKE\""
            )
            buildConfigField("String", "IMAGE_BASE_URL", "\"https://image.tmdb.org/t/p/w500\"")
        }
    }
    flavorDimensions += listOf("platform", "env")

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation)
    implementation(libs.navigation.fragment)
    implementation(libs.coil)

    //Async Operations
    implementation(libs.coroutines)

    //DI
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    //HTTP Request
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    //HTTP Request Realtime Viewer
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)

    implementation(libs.exoplayer)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}