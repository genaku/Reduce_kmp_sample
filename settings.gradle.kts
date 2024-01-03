rootProject.name = "Reduce"
include(":composeApp")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven {
            name = "myReduceRepo"
            credentials(PasswordCredentials::class)
            url  = uri("https://s01.oss.sonatype.org/service/local/repositories/iogithubgenaku-1003/content/")

        }
    }
}
