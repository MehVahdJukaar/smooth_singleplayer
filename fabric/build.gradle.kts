plugins {
    id("com.possible-triangle.fabric")
}

fabric {
    dependOn(project(":common"))
    accessWidener(project(":common"))
}

dependencies {
    modRuntimeOnly("curse.maven:sodium-394468:8396480")

    modCompileOnly("maven.modrinth:modmenu:18.0.2")
    modLocalRuntime("maven.modrinth:modmenu:18.0.2")
}
