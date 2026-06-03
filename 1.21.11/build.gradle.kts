plugins {
    id("multiplatform-common")
    id("modrinth-publish")
}

allprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.release.set(21)
    }
}
