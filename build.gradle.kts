plugins {
    id("java")
    id("io.papermc.paperweight.userdev") version "1.5.3"
    id("maven-publish")
}

group = "de.erethon.lectern"
version = "1.0-SNAPSHOT"
val papyrusVersion = "1.20.4-R0.1-SNAPSHOT"
val correctJarName = "${project.name}-${project.version}.jar"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
    maven("https://erethon.de/repo")
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    paperweight.devBundle("de.erethon.papyrus", papyrusVersion) { isChanging = true }
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "${project.group}"
            artifactId = "Lectern"
            version = "${project.version}"
            from(components["java"])
        }
    }
}
tasks {
    assemble {
        dependsOn(reobfJar)
    }
    jar {
        archiveFileName.set(correctJarName)
    }

    reobfJar {
        outputJar.set(layout.buildDirectory.file("libs/$correctJarName"))
    }
}