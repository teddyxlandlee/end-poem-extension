plugins {
    id("universal-common")
    id("modrinth-publish")
//    id("fabric-api-module")
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

dependencies {
    compileOnly("xland.mcmod:enchlevel-langpatch:3.1.0")

//    implementation(fabricApiModule("fabric-resource-loader-v1", "0.150.2+26.2"))
    implementation("net.fabricmc.fabric-api:fabric-resource-loader-v1:2.0.12+d3d844724f")
}
