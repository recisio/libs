plugins {
    id("com.google.devtools.ksp")
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

group = "com.recisio.compose_mock_preview"
version = "0.0.1"

repositories {
    mavenCentral()
}

sourceSets.main {
    resources.srcDirs("src/main/resources")
}

dependencies {
    implementation(kotlin("stdlib"))
    // KotlinPoet pour génération KSP
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)

    // KSP API
    implementation(libs.symbol.processing.api)
    implementation(kotlin("stdlib-jdk8"))
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "com.recisio.libs"
            artifactId = "compose-mock-preview"
            version = "0.0.1"
        }
    }
}



