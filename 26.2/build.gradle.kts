plugins {
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT"
    id("com.gradleup.shadow") version "9.4.2"

    id("modrinth-publish")
}

val mcVersion: String = project.ext["minecraft_version"].toString()

val shaded by configurations.creating

dependencies {
    implementation(project(":common"))
    add("shaded", project(":common")) {
        isTransitive = false
    }

    minecraft("com.mojang:minecraft:$mcVersion")

    val mixinVersion = "0.17.0+mixin.0.8.7"
    val mixinExtrasVersion = "0.5.3"    // For NeoForge 26.1.0.x
    implementation("net.fabricmc:sponge-mixin:${mixinVersion}")
    compileOnly("io.github.llamalad7:mixinextras-common:$mixinExtrasVersion")
    annotationProcessor("io.github.llamalad7:mixinextras-common:$mixinExtrasVersion")
}

tasks.processResources {
    val properties = mapOf("version" to "${project.version}+$mcVersion")
    inputs.properties(properties)
    filteringCharset = "UTF-8"

    filesMatching(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml", "fabric.mod.json")) {
        expand(properties)
    }
}

tasks.withType<Jar>().configureEach {
    archiveAppendix.set(project.name)
    archiveVersion.set("${project.version}+$mcVersion")
}

tasks.shadowJar {
    configurations = listOf(shaded)
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}
