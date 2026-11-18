plugins {
    id("com.possible-triangle.fabric")
}

fabric {
    dependOn(project(":common"))
    accessWidener(project(":common"))
}

dependencies {
    modRuntimeOnly("maven.modrinth:sodium:mc1.20.1-0.5.13-fabric")

    modCompileOnly("maven.modrinth:modmenu:7.2.2")
    modLocalRuntime("maven.modrinth:modmenu:7.2.2")
}
