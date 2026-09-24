dependencies {
    compileOnly("xland.mcmod:enchlevel-langpatch:3.9.1")
    val fabricApiVersion = "0.92.12+1.20.1"
    modImplementation(fabricApi.module("fabric-resource-loader-v0", fabricApiVersion))
}
