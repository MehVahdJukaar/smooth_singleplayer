plugins {
    id("com.possible-triangle.neoforge")
}

neoforge {
    dependOn(project(":common"))
    accessWidener(project(":common"))
}

dependencies {
    runtimeOnly("net.mehvahdjukaar:moonlight-neoforge:1.21.1-3.8.3")
    runtimeOnly("net.mehvahdjukaar:snowyspirit-neoforge:1.21.1-3.2.0")
    runtimeOnly("maven.modrinth:stormies-spiders:3.3.2-neoforge")
}
