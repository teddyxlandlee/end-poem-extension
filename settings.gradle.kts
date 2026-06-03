pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net") {
            name = "Fabric"
        }
        maven("https://maven.architectury.dev")
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.minecraftforge.net")
        gradlePluginPortal()
        maven("https://maven.hixland.com")
    }

    includeBuild("build-logic")
}

include("common")
include("1.20.1", "1.20.1:fabric", "1.20.1:forge")
include("1.21.1", "1.21.1:fabric", "1.21.1:neoforge")