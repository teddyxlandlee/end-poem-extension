plugins {
    id("universal-common")
    id("modrinth-publish")
//    id("fabric-api-module")
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

dependencies {
    compileOnly("xland.mcmod:enchlevel-langpatch:3.9.1")

//    implementation(fabricApiModule("fabric-resource-loader-v1", "0.160.4+26.3"))
    implementation("net.fabricmc.fabric-api:fabric-resource-loader-v1:3.0.4+fcdff87f5d")
    implementation("net.neoforged.fancymodloader:loader:12.0.3")

    val mixinVersion = "0.17.4+mixin.0.8.7"
    val mixinExtrasVersion = "0.5.5"
    implementation("net.fabricmc:sponge-mixin:${mixinVersion}")
    compileOnly("io.github.llamalad7:mixinextras-common:$mixinExtrasVersion")
    annotationProcessor("io.github.llamalad7:mixinextras-common:$mixinExtrasVersion")
}
