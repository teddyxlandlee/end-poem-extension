import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar.Companion.shadowJar

plugins {
    id("architectury-plugin")
    id("dev.architectury.loom")
}

val mcVersion: String = project.ext["minecraft_version"].toString()
val projectPath = project.path

architectury {
    minecraft = mcVersion
    common(project.ext["mod_platforms"].toString().split(','))
}

configurations {
    val epxCommon by creating
    compileClasspath {
        extendsFrom(epxCommon)
    }
    runtimeClasspath {
        extendsFrom(epxCommon)
    }
}

loom {
    @Suppress("UnstableApiUsage")
    mixin {
        defaultRefmapName = "epx.refmap.json"
    }
}

val mixinVersion = "0.14.0+mixin.0.8.6"
val mixinExtrasVersion = "0.3.5"    // Used by NeoForge 21.1.x

dependencies {
    add("epxCommon", project(":common"))
    implementation("net.fabricmc:sponge-mixin:$mixinVersion")
    compileOnly("io.github.llamalad7:mixinextras-common:$mixinExtrasVersion")
    annotationProcessor("io.github.llamalad7:mixinextras-common:$mixinExtrasVersion")
}

tasks.withType<Jar>().configureEach {
    archiveAppendix.set("common")
}

allprojects {
    apply(plugin="architectury-plugin")
    apply(plugin="dev.architectury.loom")

    loom {
        silentMojangMappingsLicense()
    }

    dependencies {
        minecraft("com.mojang:minecraft:$mcVersion")
        mappings(loom.officialMojangMappings())
    }

    java.withSourcesJar()

    tasks.withType<AbstractArchiveTask>().configureEach {
        archiveVersion.set("${project.version}+$mcVersion")
    }
}

enum class ModPlatform(
    private val projectName: String,
    val transformProductionName: String,
    val developmentConfigurationName: String,
) {
    FABRIC("fabric", "transformProductionFabric", "developmentFabric"),
    FORGE("forge", "transformProductionForge", "developmentForge"),
    NEO("neoforge", "transformProductionNeoForge", "developmentNeoForge"),
    ;

    companion object {
        fun ofProject(project: Project): ModPlatform = entries.first {
            it.projectName.equals(project.name, ignoreCase = true)
        }
    }
}

val Project.modPlatform get() = ModPlatform.ofProject(this)

subprojects {
    apply(plugin="com.gradleup.shadow")

    architectury {
        platformSetupLoomIde()
        when (modPlatform) {
            ModPlatform.FABRIC -> fabric()
            ModPlatform.FORGE -> forge()
            ModPlatform.NEO -> neoForge()
        }
    }

    val common by configurations.creating
    val shadowCommon by configurations.creating
    configurations.compileClasspath {
        extendsFrom(common)
    }
    configurations.runtimeClasspath {
        extendsFrom(common)
    }
    configurations.maybeCreate(modPlatform.developmentConfigurationName).apply {
        extendsFrom(common)
    }

    dependencies {
        add("common", project(":common"))
        add("shadowCommon", project(":common")) {
            isTransitive = false
        }

        add("common", project(path=projectPath, configuration="namedElements")) {
            isTransitive = false
        }
        add("shadowCommon", project(path=projectPath, configuration=modPlatform.transformProductionName)) {
            isTransitive = false
        }

        when (project.name) {
            "fabric" -> {
                val fabricLoaderVersion: String = rootProject.ext["fabric_loader_version"].toString()
                implementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
            }
            "forge" -> {
                compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:$mixinExtrasVersion")!!)
                implementation(include("io.github.llamalad7:mixinextras-forge:$mixinExtrasVersion")!!)
            }
        }
    }

    tasks.processResources {
        val properties = mapOf("version" to "${project.version}+$mcVersion")
        inputs.properties(properties)
        filteringCharset = "UTF-8"

        filesMatching(listOf("META-INF/mods.toml", "META-INF/neoforge.mods.toml", "fabric.mod.json")) {
            expand(properties)
        }
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        archiveAppendix.set(project.name)
    }

    tasks.jar {
        archiveClassifier.set("dev")
    }

    tasks.shadowJar {
        exclude("architectury.common.json")
        configurations = listOf(shadowCommon)
        archiveClassifier.set("dev-shadow")
    }

    tasks.remapJar {
        dependsOn(tasks.shadowJar)
        inputFile.set(tasks.shadowJar.flatMap { it.archiveFile })
        archiveClassifier.set(null as String?)
    }

//    tasks.getByName<AbstractArchiveTask>("sourcesJar") {
//        val parentProject = findProject(projectPath) ?: error("Parent project not found")
//        val parentSource = parentProject.tasks.getByName<AbstractArchiveTask>("sourcesJar")
//        dependsOn(parentSource)
//        from(parentSource.archiveFile.map { zipTree(it) })
//    }
}
