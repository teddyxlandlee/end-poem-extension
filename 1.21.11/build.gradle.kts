plugins {
    id("multiplatform-common")
}

allprojects {
    tasks.withType<JavaCompile>().configureEach {
        options.release.set(21)
    }
}
