plugins {
    id("ru.mipt.npm.mpp")
}

val ktorVersion: String by rootProject.extra

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":controls-core"))
                api("io.ktor:ktor-network:$ktorVersion")
            }
        }

    }
}