plugins {
    id("com.possible-triangle.forge")
}

forge {
    dependOn(project(":common"))
    accessWidener(project(":common"))
    enableMixins()
}

mixin {
    config("smooth_animations-common.mixins.json")
    config("smooth_animations.mixins.json")
}

dependencies {
    modCompileOnly("maven.modrinth:embeddium:0.3.31+mc1.20.1")
    modRuntimeOnly("maven.modrinth:embeddium:0.3.31+mc1.20.1")
}

tasks.named("compileJava") {
    dependsOn(":common:candleLightTransform")
}
