plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    cocoapods {
        summary = "Shared data access and business logic module for Orto's frontends."
        homepage = "https://codeberg.org/uwutech/Orto"
        version = "0.1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = false
            transitiveExport = true
            embedBitcode(org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode.BITCODE)

        }
        // Maps custom Xcode configuration to NativeBuildType
        xcodeConfigurationToNativeBuildType["debug"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG
        xcodeConfigurationToNativeBuildType["release"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.RELEASE
        xcodeConfigurationToNativeBuildType["Preview-D"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.DEBUG
    }

    val jUnitVersion = "4.13.2"

    sourceSets {
        val commonMain by getting {
            dependencies {
                with(Deps.KotlinX) {
                    implementation(datetime)
                }
                with (Deps.KotlinX.Coroutines) {
                    implementation(core)
                }
                with(Deps.Ktor) {
                    implementation(core)
                    implementation(content)
                    implementation(serializationJson)
                    implementation(logging)
                }
                with(Deps.SqlDelight) {
                    implementation(runtime)
                    implementation(coroutines)
                }
                with(Deps.Koin) {
                    implementation(core)
                    implementation(android)
                }
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                with (Deps.KotlinX.Coroutines) {
                    implementation(test)
                }
                with(Deps.SqlDelight) {
                    implementation(sqlite)
                    implementation(jdbc)
                }
                with(Deps.Mockk) {
                    implementation(common)
                }
                with(Deps.Koin) {
                    implementation(test)
                }
            }
        }
        val androidMain by getting {
            dependencies {
                with (Deps.KotlinX.Coroutines) {
                    implementation(android)
                }
                with(Deps.Ktor) {
                    implementation(android)
                }
                with(Deps.SqlDelight) {
                    implementation(android)
                }
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:${jUnitVersion}")
                //need to add
                implementation("androidx.test:core:1.5.0")
                with(Deps.Mockk) {
                    implementation(jvm)
                }
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                with(Deps.Ktor) {
                    implementation(darwin)
                }
                with(Deps.SqlDelight) {
                    implementation(native)
                }
            }
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "coop.uwutech.orto"
    compileSdk = Versions.compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
    }
}

sqldelight {
    database("OrtoDatabase") {
        packageName = "coop.uwutech.orto.shared.cache"
    }
}