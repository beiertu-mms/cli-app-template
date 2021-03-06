import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"

    id("com.github.johnrengelman.shadow") version "7.1.2"

    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.github.ajalt.clikt:clikt:3.5.0")
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    }

    register<Copy>("packageDistribution") {
        dependsOn("shadowJar")
        from("${project.rootDir}/scripts/cli-app-template.sh")
        from("${project.buildDir}/libs/${project.name}.jar")
        into("${project.buildDir}/dist")
    }
}

application {
    // Define the main class for the application.
    mainClass.set("de.beiertu.cli.template.AppKt")
}

tasks.withType<ShadowJar> {
    archiveBaseName.set("cli-app-template")
    archiveClassifier.set("")
    archiveVersion.set("")
}
