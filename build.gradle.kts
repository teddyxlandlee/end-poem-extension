import java.time.Instant
import java.time.temporal.ChronoUnit

plugins {
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT"
    id("com.modrinth.minotaur") version "2.+" apply false

    `maven-publish`
}

version = rootProject.ext["mod_version"]!!
group = rootProject.ext["maven_group"]!!

repositories {
    maven("https://maven.neoforged.net/releases") {
        name = "NeoForged Maven"
    }
    maven("https://maven.architectury.dev")
    maven("https://maven.minecraftforge.net")
    maven("https://mvn.7c7.icu") {
        name = "7c7Maven"
    }
}

dependencies {
    add("minecraft", "com.mojang:minecraft:${rootProject.ext["minecraft_version"]}")

    implementation("net.fabricmc:fabric-loader:${rootProject.ext["fabric_loader_version"]}")
    // to insert Recommended Pack relevant info
    implementation(fabricApi.module("fabric-resource-loader-v1", rootProject.ext["fabric_api_version"].toString()))
    compileOnly("xland.mcmod:enchlevel-langpatch:3.1.0")
    compileOnly("org.jetbrains:annotations:26.1.0")
}

tasks.processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"

    filesMatching(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml", "fabric.mod.json")) {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(25)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
    withSourcesJar()
}

tasks.jar {
    archiveClassifier.set("universal")
    from(rootProject.file("LICENSE")) {
        rename { "LICENSE_end-poem-extension" }
    }
    manifest {
        attributes(
            "Specification-Title"      to "end-poem-extension",
            "Specification-Vendor"     to "teddyxlandlee",
            "Specification-Version"    to "1",
            "Implementation-Title"     to "${rootProject.name}-universal",
            "Implementation-Version"   to project.version,
            "Implementation-Vendor"    to "teddyxlandlee",
            "Implementation-Timestamp" to Instant.now().truncatedTo(ChronoUnit.SECONDS),
            // Forge compatibility
            "MixinConfigs" to "end-poem-extension.mixins.json",
        )
    }
}

providers.environmentVariable("MR_TOKEN").takeIf { it.isPresent }?.also { mrToken ->
    apply(plugin = "com.modrinth.minotaur")
    extensions.configure<com.modrinth.minotaur.ModrinthExtension>("modrinth") {
        token.set(mrToken)
        projectId.set(providers.gradleProperty("mr_project_id"))
        versionNumber.set("${project.version}-universal")
        versionName.set(provider {
            String.format(
                providers.gradleProperty("mr_version_name_format").get(),
                providers.gradleProperty("mr_version_game_range").get(),
                "Universal", // Loader Display
                providers.gradleProperty("mr_version_mod_abbr").get(),
                providers.gradleProperty("mr_version_display").get(),
            )
        })
        changelog.set(providers.gradleProperty("mr_version_changelog"))
        versionType.set(providers.gradleProperty("mr_version_type"))
        gameVersions.set(providers.gradleProperty("mr_version_game").map {
            it.split(',')
        })
        loaders.set(providers.gradleProperty("mr_loader").map {
            it.split(',')
        })
        detectLoaders.set(false)
        autoAddDependsOn.set(false)

        dependencies {
            optional.project("remote-resource-pack")
        }

        uploadFile.set(tasks.jar)
        additionalFiles.add(tasks["sourcesJar"])

        debugMode.set(providers.environmentVariable("MR_DEBUG_MODE").map { "1" == it }.orElse(false))
    }
}