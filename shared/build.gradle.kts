plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp") version Versions.ksp
    id("com.android.library")
    id("com.squareup.sqldelight")
}

// read values from gradle.properties
val artifactId: String by project
val pomDescription: String by project
val siteUrl: String by project
val pomLicenseName: String by project
val pomLicenseUrl: String by project
val pomLicenseDist: String by project
val pomDeveloperId: String by project
val pomDeveloperName: String by project
val pomOrganizationName: String by project
val pomOrganizationUrl: String by project

val emptyJavadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

group = Orto.group
version = Orto.versionName

kotlin {
    jvmToolchain(11)
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = Versions.jdk
            }
        }
    }
//    jvm()
//    linuxX64()
//    mingwX64()
//    js(IR) {
//        jvmToolchain(11)
//    }
    listOf(
        // intel
//        macosX64(),
        iosX64(),

        // apple silicon
//        macosArm64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = Orto.artifactId
            // Make AppleSettings visible from Swift
            export(Deps.Settings.settings)
            transitiveExport = true
        }
    }

    cocoapods {
        summary = "Shared data access and business logic module for Orto frontend."
        homepage = "https://codeberg.org/uwutech/Orto"
        version = Orto.versionName
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = Orto.artifactId
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                with(Deps.Koin) {
                    implementation(core)
                }
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
                with(Deps.Napier) {
                    implementation(napier)
                }
                with(Deps.OFM) {
                    implementation(ofm)
                }
                with(Deps.Settings) {
                    implementation(settings)
                }
                with(Deps.SqlDelight) {
                    implementation(runtime)
                    implementation(coroutines)
                }
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("app.cash.turbine:turbine:${Versions.turbine}")
                with (Deps.KotlinX.Coroutines) {
                    implementation(test)
                }
                with(Deps.Mockative) {
                    implementation(core)
                }
                with(Deps.Koin) {
                    implementation(test)
                }
                with(Deps.Settings) {
                    implementation(test)
                }
            }
        }
        val nativeMain by creating {
            dependsOn(commonMain)
        }
        val nativeTest by creating {
            dependsOn(commonTest)
        }
//        val jvmMain by getting {
//            dependencies {
//                implementation(Deps.Kotlin.jvm)
//            }
//            dependsOn(commonMain)
//        }
//        val jvmTest by getting {
//            dependencies {
//                implementation(Deps.Test.jvm)
//            }
//            dependsOn(commonTest)
//        }
//        val jsMain by getting {
//            dependencies {
//                implementation(Deps.Kotlin.js)
//            }
//            dependsOn(commonMain)
//        }
//        val jsTest by getting {
//            dependencies {
//                implementation(Deps.Test.js)
//            }
//            dependsOn(commonTest)
//        }
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
            dependsOn(commonMain)
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(Deps.Test.jvm)
                //need to add
                implementation("androidx.test:core:1.5.0")
                with(Deps.SqlDelight) {
                    implementation(sqlite)
                }
            }
        }
//        val linuxX64Main by getting {
//            dependsOn(nativeMain)
//        }
//        val linuxX64Test by getting {
//            dependsOn(nativeTest)
//        }
//        val mingwX64Main by getting {
//            dependsOn(nativeMain)
//        }
//        val mingwX64Test by getting {
//            dependsOn(nativeTest)
//        }

        val darwinMain by creating {
            dependencies {
                with(Deps.Ktor) {
                    implementation(darwin)
                }
                with(Deps.SqlDelight) {
                    implementation(native)
                }
            }
            dependsOn(nativeMain)
        }
        val darwinTest by creating {
            dependencies {
                with(Deps.SqlDelight) {
                    implementation(native)
                }
            }
            dependsOn(nativeTest)
        }

        // intel
//        val macosX64Main by getting {
//            dependsOn(darwinMain)
//        }
//        val macosX64Test by getting {
//            dependsOn(darwinTest)
//        }
        val iosX64Main by getting {
            dependsOn(darwinMain)
        }
        val iosX64Test by getting {
            dependsOn(darwinTest)
        }
        // apple silicon
//        val macosArm64Main by getting {
//            dependsOn(darwinMain)
//        }
//        val macosArm64Test by getting {
//            dependsOn(darwinTest)
//        }
        val iosArm64Main by getting {
            dependsOn(darwinMain)
        }
        val iosArm64Test by getting {
            dependsOn(darwinTest)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(darwinMain)
        }
        val iosSimulatorArm64Test by getting {
            dependsOn(darwinTest)
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
    namespace = Orto.group
    compileSdk = Versions.compileSdk
    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

sqldelight {
    database("OrtoDatabase") {
        packageName = "garden.orto.shared.cache"
    }
}


// Will be fixed in Kotlin 1.9
// See https://youtrack.jetbrains.com/issue/KT-55751
val myAttribute = Attribute.of("myOwnAttribute", String::class.java)

// replace releaseFrameworkIosFat by the name of the first configuration that conflicts
configurations.named("podDebugFrameworkIosFat").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "pod-debug-all")
    }
}

// replace debugFrameworkIosFat by the name of the second configuration that conflicts
configurations.named("debugFrameworkIosFat").configure {
    attributes {
        attribute(myAttribute, "debug-all")
    }
}

// replace releaseFrameworkIosFat by the name of the first configuration that conflicts
configurations.named("podDebugFrameworkIosSimulatorArm64").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "simulator-pod-debug-all")
    }
}

// replace debugFrameworkIosFat by the name of the second configuration that conflicts
configurations.named("debugFrameworkIosSimulatorArm64").configure {
    attributes {
        attribute(myAttribute, "simulator-debug-all")
    }
}

// replace releaseFrameworkIosFat by the name of the first configuration that conflicts
configurations.named("podDebugFrameworkIosX64").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "x64-pod-debug-all")
    }
}

// replace debugFrameworkIosFat by the name of the second configuration that conflicts
configurations.named("debugFrameworkIosX64").configure {
    attributes {
        attribute(myAttribute, "x64-debug-all")
    }
}

// replace releaseFrameworkIosFat by the name of the first configuration that conflicts
configurations.named("releaseFrameworkIosArm64").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "arm64-release-all")
    }
}

// replace debugFrameworkIosFat by the name of the second configuration that conflicts
configurations.named("podReleaseFrameworkIosArm64").configure {
    attributes {
        attribute(myAttribute, "arm64-pod-release-all")
    }
}

// replace releaseFrameworkIosFat by the name of the first configuration that conflicts
configurations.named("releaseFrameworkIosFat").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "ios-release-all")
    }
}

// replace debugFrameworkIosFat by the name of the second configuration that conflicts
configurations.named("podReleaseFrameworkIosFat").configure {
    attributes {
        attribute(myAttribute, "ios-pod-release-all")
    }
}

// replace releaseFrameworkIosFat by the name of the first configuration that conflicts
configurations.named("releaseFrameworkIosSimulatorArm64").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "simulator-release-all")
    }
}

// replace debugFrameworkIosFat by the name of the second configuration that conflicts
configurations.named("podReleaseFrameworkIosSimulatorArm64").configure {
    attributes {
        attribute(myAttribute, "simulator-pod-release-all")
    }
}

// replace releaseFrameworkIosFat by the name of the first configuration that conflicts
configurations.named("releaseFrameworkIosX64").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "x64-release-all")
    }
}

// replace debugFrameworkIosFat by the name of the second configuration that conflicts
configurations.named("podReleaseFrameworkIosX64").configure {
    attributes {
        attribute(myAttribute, "x64-pod-release-all")
    }
}

// replace releaseFrameworkIosFat by the name of the first configuration that conflicts
configurations.named("podDebugFrameworkIosArm64").configure {
    attributes {
        // put a unique attribute
        attribute(myAttribute, "arm64-pod-debug-all")
    }
}

// replace debugFrameworkIosFat by the name of the second configuration that conflicts
configurations.named("debugFrameworkIosArm64").configure {
    attributes {
        attribute(myAttribute, "arm64-debug-all")
    }
}
