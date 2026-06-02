plugins {
    java
    id("com.modrinth.minotaur") version "2.+" apply false
}

allprojects {
    version = rootProject.ext["mod_version"]!!
    group = rootProject.ext["maven_group"]!!

    apply(plugin="java")

    repositories {
        maven("https://maven.neoforged.net/releases") {
            name = "NeoForged Maven"
        }
        maven("https://maven.architectury.dev")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.fabricmc.net")
        maven("https://libraries.minecraft.net")
        mavenCentral()
        maven("https://maven.hixland.com") {
            name = "Teddy's Maven"
        }
    }

    dependencies {
        add("compileOnly", "org.jetbrains:annotations:26.1.0")
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
    }

    tasks.jar {
        from(rootProject.file("LICENSE")) {
            rename { "LICENSE_end-poem-extension" }
        }
    }
}