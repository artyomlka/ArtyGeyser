@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()

        maven("https://repo.opencollab.dev/maven-snapshots/")
    }
    includeBuild("build-logic")
}

rootProject.name = "geyser-parent"

include(":ap")
include(":api")
include(":velocity")
include(":common")
include(":core")

// Specify project dirs
project(":velocity").projectDir = file("bootstrap/velocity")

// Allow to download JVMs for toolchains
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("1.0.0")
}
