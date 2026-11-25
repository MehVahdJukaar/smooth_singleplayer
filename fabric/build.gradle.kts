plugins {
    id("com.possible-triangle.fabric")
}

fabric {
    dependOn(project(":common"))
    accessWidener(project(":common"))
}

dependencies {
    modRuntimeOnly("curse.maven:sodium-394468:8396480")
    modRuntimeOnly("net.mehvahdjukaar:moonlight-fabric:26.1.2-4.3.4")
    modRuntimeOnly("net.mehvahdjukaar:codecui-fabric:26.1.2-1.4.10")
    modRuntimeOnly("net.mehvahdjukaar:snowyspirit-fabric:26.1.2-3.3.0")

    modCompileOnly("maven.modrinth:modmenu:18.0.2")
    localRuntime("maven.modrinth:modmenu:18.0.2")
}
