plugins {
    id("universal-common")
    id("modrinth-publish")
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}
