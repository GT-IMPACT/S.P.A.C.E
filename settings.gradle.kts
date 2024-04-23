@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {

    repositories {
        maven("https://maven.accident.space/repository/maven-public/")
        maven("https://nexus.gtnewhorizons.com/repository/public/")
        maven("https://maven.minecraftforge.net")
        maven("https://plugins.gradle.org/m2/")
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://maven.accident.space/repository/maven-public/")
        maven("https://nexus.gtnewhorizons.com/repository/public/")
        maven("https://maven.minecraftforge.net")
        maven("https://plugins.gradle.org/m2/")
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "space"

includeBuild("build-logic")