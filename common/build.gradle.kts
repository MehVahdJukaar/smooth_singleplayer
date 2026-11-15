import com.possible_triangle.gradle.access.generateAccessTransformer
import com.possible_triangle.gradle.commonMixinDependencies
import com.possible_triangle.gradle.features.loaders.configureCommonProject
import com.possible_triangle.gradle.mod
import com.possible_triangle.gradle.neoforge.configureParchment
import com.possible_triangle.gradle.neoforge.sharedConfiguration
import com.possible_triangle.gradle.stringProperty
import net.neoforged.nfrtgradle.CreateMinecraftArtifacts

// helper's common plugin wants neoform, which starts at 1.20.2. its aw->at remap also needs forge userdev, so common sees forge too. careful
plugins {
    id("net.neoforged.moddev.legacyforge")
}

configureCommonProject()
commonMixinDependencies()

val (generatedAT, generateATTask) = generateAccessTransformer(mod.id.map { file("src/main/resources/$it.accesswidener") })

legacyForge {
    version = "${mod.minecraftVersion.get()}-${stringProperty("forge_version").get()}"

    sharedConfiguration(project)
    configureParchment(project, stringProperty("parchment_mappings_version"))

    accessTransformers {
        from(generatedAT)
        publish(generatedAT)
    }
}

tasks.withType<CreateMinecraftArtifacts> {
    dependsOn(generateATTask)
}
tasks.named("copyAccessTransformersPublications") {
    dependsOn(generateATTask)
}
