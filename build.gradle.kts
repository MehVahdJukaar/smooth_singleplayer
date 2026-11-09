
plugins {
    id("com.possible-triangle.core") version "1.4.259"
    id("com.possible-triangle.common") version "1.4.259" apply false
    id("com.possible-triangle.fabric") version "1.4.259" apply false
    id("com.possible-triangle.neoforge") version "1.4.259" apply false
    id("net.mehvahdjukaar.candlelight") version "1.2.4" apply false
}

mod {
    val mod_description: String by extra
    val mod_credits: String by extra
    val mod_license: String by extra
    val mod_homepage: String by extra
    val mod_github: String by extra
    val mod_authors: String by extra
    val moonlight_min_version: String by extra
    additional.add("mod_description", provider { mod_description })
    additional.add("mod_credits", provider { mod_credits })
    additional.add("mod_license", provider { mod_license })
    additional.add("mod_homepage", provider { mod_homepage })
    additional.add("mod_authors", provider { mod_authors })
    additional.add("mod_github", provider { mod_github })
    additional.add("moonlight_min_version", provider { moonlight_min_version })
}


subprojects {

    apply(plugin = "com.possible-triangle.core")
    apply(plugin = "net.mehvahdjukaar.candlelight")
    apply(plugin = "maven-publish")

    dependencies {
        compileOnly("net.mehvahdjukaar:candlelight:1.2.4")
    }


    tasks.withType<GenerateModuleMetadata>().configureEach {
        enabled = true
    }

    repositories {
        nexus()
    }



    upload {
        maven {
            nexus()
        }
        curseforge {
            dependencies {
                required("selene")
            }
        }
        modrinth {
            dependencies {
                required("moonlight")
            }
        }

        forEach {
            changelog = rootProject.file("changelog.md").readText()
            versionName = "${mod.id.get()}-${mod.version.get()}-${project.name}"
        }
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(listOf("-Xmaxerrs", "4000"))
    }


    repositories {
        // Standard repositories
        mavenLocal()
        mavenCentral()

        flatDir {
            dirs("mods")
        }

        maven { url = uri("https://jitpack.io") }

        maven { url = uri("https://maven.neoforged.net/releases") }
        maven { url = uri("https://maven.architectury.dev") }
        maven { url = uri("https://maven.parchmentmc.org") }
        maven { url = uri("https://maven.neoforged.net") }

        maven { url = uri("https://api.modrinth.com/maven") } // Modrinth (Sodium, etc.)
    }
}
