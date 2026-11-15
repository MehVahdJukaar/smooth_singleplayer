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
}

tasks.named("compileJava") {
    dependsOn(":common:candleLightTransform")
}
