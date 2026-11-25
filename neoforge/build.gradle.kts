plugins {
    id("com.possible-triangle.neoforge")
}

neoforge {
    dependOn(project(":common"))
    accessWidener(project(":common"))
}

dependencies {
    runtimeOnly("net.mehvahdjukaar:moonlight-neoforge:26.1.2-4.3.4")
    runtimeOnly("net.mehvahdjukaar:snowyspirit-neoforge:26.1.2-3.3.0")
}
