plugins {
    id("com.possible-triangle.fabric")
}

fabric {
    dependOn(project(":common"))
    accessWidener(project(":common"))
}

val moonlight_version: String by extra

dependencies {
    modImplementation("net.mehvahdjukaar:moonlight-fabric:${moonlight_version}")

    modCompileOnly("com.terraformersmc:modmenu:11.0.3")
    modRuntimeOnly("maven.modrinth:sodium:mc1.21.1-0.8.12-fabric")
}
