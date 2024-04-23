plugins {
    alias(libs.plugins.buildconfig)
    id("minecraft")
}

repositories {
    maven("https://maven.accident.space/repository/maven-public/")
    maven("https://nexus.gtnewhorizons.com/repository/public/")
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://cursemaven.com")
    mavenLocal()
}

dependencies {
    api("space.impact:forgelin:2.0.+") { isChanging = true }
    api("space.impact:packet_network:1.1.3:dev")
    api("com.github.GTNewHorizons:NotEnoughItems:2.4.13-GTNH:dev")
//    api("net.industrial-craft:industrialcraft-2:2.2.828-experimental:dev")
    api("curse.maven:ic2-242638:2353971")
    runtimeOnlyNonPublishable("curse.maven:journeymap-32274:4500659")
}

val modId: String by extra

tasks.runClient.configure {
    extraArgs.addAll("--mixin", "mixins.$modId.json")
}
