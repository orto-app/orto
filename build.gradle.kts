plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version(Versions.androidApplication).apply(false)
    id("com.android.library").version(Versions.androidApplication).apply(false)
    kotlin("android").version(Versions.kotlin).apply(false)
    kotlin("multiplatform").version(Versions.kotlin).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

buildscript {

    dependencies {
        classpath(Deps.Gradle.kotlin)
        classpath(Deps.Gradle.kotlinSerialization)
        classpath(Deps.Gradle.navigation)
        classpath(Deps.Gradle.sqldelight)
        classpath(Deps.Gradle.gradle)
    }
}