pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins{
        id("com.google.devtools.ksp") version "2.1.21-2.0.2"
        kotlin("jvm") version "2.1.21"
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Preview-compose"
include(":app")
include(":compose-mock-preview")
