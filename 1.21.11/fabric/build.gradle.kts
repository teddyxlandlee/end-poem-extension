dependencies {
    compileOnly("xland.mcmod:enchlevel-langpatch:3.9.1")
    val fabricApiVersion = "0.141.6+1.21.11"
    modImplementation(fabricApi.module("fabric-resource-loader-v1", fabricApiVersion))
}
