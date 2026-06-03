plugins {
    java
    id("com.modrinth.minotaur") version "2.+" apply false
    id("com.gradleup.shadow") version "9.4.2" apply false
}

tasks.register("modrinthPublishAll") {
    description = "Publish all subversions to Modrinth"
}

allprojects {
    version = rootProject.ext["mod_version"]!!
    group = rootProject.ext["maven_group"]!!

    apply(plugin="java")

    base {
        archivesName = rootProject.ext["archives_base_name"].toString()
    }

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

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    tasks.jar {
        from(rootProject.file("LICENSE")) {
            rename { "LICENSE_end-poem-extension" }
        }
    }

    java.toolchain {
        // Max support 26.2+
        languageVersion = JavaLanguageVersion.of(25)
    }

    java.withSourcesJar()
}