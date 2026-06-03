dependencies {
    compileOnly("xland.mcmod:enchlevel-langpatch:3.1.0")
    val fabricApiVersion = "0.141.4+1.21.11"
    modImplementation(fabricApi.module("fabric-resource-loader-v1", fabricApiVersion))
}
