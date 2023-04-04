plugins {
    id("com.android.application")
    kotlin("android")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "garden.orto.android"
    compileSdk = Versions.compileSdk
    defaultConfig {
        applicationId = "garden.orto.android"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = Orto.versionCode
        versionName = Orto.versionName
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.kotlinCompilerExtensionVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/versions/9/previous-compilation-data.bin"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":shared"))

    with(Deps.Navigation) {
        implementation(fragment)
        implementation(ui)
        implementation(features)
        implementation(testing)
        implementation(compose)
    }

    with(Deps.Compose) {
        implementation(material3)
        implementation(material3Window)
        implementation(material)
        implementation(foundation)
        implementation(ui)
        implementation(uiToolingPreview)
        implementation(activity)
        implementation(viewmodel)
        implementation(coil)
    }
    debugImplementation("androidx.compose.ui:ui-tooling:${Versions.composeUi}")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Versions.composeUi}")
    //debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    with (Deps.KotlinX.Coroutines) {
        implementation(android)
    }
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")

    with(Deps.Koin) {
        implementation(android)
    }
    implementation("io.insert-koin:koin-androidx-compose:3.4.2")
}
repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}
