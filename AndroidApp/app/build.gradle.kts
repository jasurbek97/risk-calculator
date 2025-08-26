plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

val RELEASE_KEY_ALIAS: String by project
val RELEASE_STORE_PASSWORD: String by project
val RELEASE_KEY_PASSWORD: String by project

android {
    namespace = "uz.jastechno.riskc"
    compileSdk = 36

    defaultConfig {
        applicationId = "uz.jastechno.riskc"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug")
        {
            storeFile = file("config/release.keystore")
            keyAlias = RELEASE_KEY_ALIAS
            storePassword = RELEASE_STORE_PASSWORD
            keyPassword = RELEASE_KEY_PASSWORD
        }

        create("release")
        {
            storeFile = file("config/release.keystore")
            keyAlias = RELEASE_KEY_ALIAS
            storePassword = RELEASE_STORE_PASSWORD
            keyPassword = RELEASE_KEY_PASSWORD
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
        release {
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}