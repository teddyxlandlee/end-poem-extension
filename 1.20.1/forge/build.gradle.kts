val forgeVersion = "47.4.10"

dependencies {
    forge("net.minecraftforge:forge:1.20.1-$forgeVersion")
}

loom {
    forge {
        mixinConfigs("epx-1.20.1.mixins.json")
    }
}
