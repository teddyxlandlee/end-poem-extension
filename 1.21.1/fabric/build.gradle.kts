dependencies {
    compileOnly("xland.mcmod:enchlevel-langpatch:3.9.1")
    val fabricApiVersion = "0.116.17+1.21.1"
    modImplementation(fabricApi.module("fabric-resource-loader-v0", fabricApiVersion))
}
