plugins {
    alias(deps.plugins.android.application)
    alias(deps.plugins.kotlin.android)
    alias(deps.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
}

android {
    namespace = "com.notasapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.notasapp"
        minSdk = 30
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(deps.androidx.core.ktx)
    implementation(deps.androidx.lifecycle.runtime.ktx)
    implementation(deps.androidx.activity.compose)

    implementation(platform(deps.androidx.compose.bom))
    implementation(deps.androidx.compose.ui)
    implementation(deps.androidx.compose.ui.graphics)
    implementation(deps.androidx.compose.ui.tooling.preview)
    implementation(deps.androidx.compose.material3)
    implementation(deps.androidx.compose.material.icons.extended)

    implementation(deps.androidx.lifecycle.viewmodel.compose)
    implementation(deps.androidx.lifecycle.runtime.compose)

    implementation(deps.androidx.navigation.compose)

    implementation(deps.room.runtime)
    implementation(deps.room.ktx)
    ksp(deps.room.compiler)

    implementation(deps.kotlinx.coroutines.android)
    implementation(deps.kotlinx.coroutines.core)

    implementation(deps.retrofit)
    implementation(deps.retrofit.gson)

    testImplementation(deps.junit)

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
