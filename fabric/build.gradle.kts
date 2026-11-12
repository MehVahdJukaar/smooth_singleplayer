plugins {
    id("com.possible-triangle.fabric")
}

fabric {
    dependOn(project(":common"))
    accessWidener(project(":common"))
}

dependencies {
    modRuntimeOnly("maven.modrinth:sodium:mc1.21.1-0.8.12-fabric")
}
