plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.architectury.dev")
    maven("https://maven.neoforged.net/releases")
    maven("https://maven.minecraftforge.net")
    maven("https://maven.fabricmc.net")
}

dependencies {
    gradleApi()
    implementation(gradleKotlinDsl())

    implementation("architectury-plugin:architectury-plugin.gradle.plugin:3.5-SNAPSHOT")
    implementation("dev.architectury:architectury-loom:1.13-SNAPSHOT")
    implementation("com.gradleup.shadow:shadow-gradle-plugin:9.4.2")

    implementation("com.modrinth.minotaur:Minotaur:2.+")
}