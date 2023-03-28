plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp") version "1.8.0-1.0.9"
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
        summary = "Shared data access and business logic module for Orto frontend."
        homepage = "https://codeberg.org/uwutech/Orto"
        version = Orto.versionName
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }

    val jUnitVersion = "4.13.2"

    sourceSets {
        val commonMain by getting {
            dependencies {
                with(Deps.KotlinX) {
                    implementation(datetime)
                }
                with(Deps.KotlinX.Coroutines) {
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
                with(Deps.Mockative) {
                    implementation(core)
                }
                with(Deps.Koin) {
                    implementation(test)
                }
            }
        }
        val androidMain by getting {
            dependencies {
                with(Deps.KotlinX.Coroutines) {
                    implementation(android)
                }
                with(Deps.Ktor) {
                    implementation(android)
                }
                with(Deps.SqlDelight) {
                    implementation(android)
                }
                with(Deps.Koin) {
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
                with(Deps.SqlDelight) {
                    implementation(sqlite)
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
            dependencies {
                with(Deps.SqlDelight) {
                    implementation(native)
                }
            }
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

dependencies {
    configurations
        .filter { it.name.startsWith("ksp") && it.name.contains("Test") }
        .forEach {
            add(it.name, Deps.Mockative.processor)
        }
}

android {
    namespace = "coop.uwutech.orto"
    compileSdk = Versions.compileSdk
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
