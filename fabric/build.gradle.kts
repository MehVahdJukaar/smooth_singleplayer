plugins {
    id("com.possible-triangle.fabric")
}

fabric {
    dependOn(project(":common"))
    accessWidener(project(":common"))
}

dependencies {
    modRuntimeOnly("maven.modrinth:sodium:mc1.21.1-0.8.12-fabric")
    modRuntimeOnly("net.mehvahdjukaar:moonlight-fabric:1.21.1-3.8.3")
    modRuntimeOnly("net.mehvahdjukaar:codecui-fabric:1.21.1-1.4.10")
    modRuntimeOnly("net.mehvahdjukaar:snowyspirit-fabric:1.21.1-3.2.0")

    modCompileOnly("maven.modrinth:modmenu:11.0.3")
    modLocalRuntime("maven.modrinth:modmenu:11.0.3")
}
