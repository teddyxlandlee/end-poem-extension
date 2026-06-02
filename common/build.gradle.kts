plugins {
    `java-library`
}

tasks.withType<JavaCompile>().configureEach {
    // Minimum support: Java Edition 1.20.1
    options.release = 17
}

dependencies {
    api("com.mojang:logging:1.1.1")
    api("org.slf4j:slf4j-api:2.0.1")
    api("com.google.code.gson:gson:2.10")
    api("com.google.guava:guava:31.1-jre")
    api("it.unimi.dsi:fastutil:8.5.9")
}