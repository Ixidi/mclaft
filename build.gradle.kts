plugins {
    kotlin("jvm") version "1.4.31" apply false
    id("com.github.johnrengelman.shadow") version "5.2.0" apply false
}

allprojects {
    group = "xyz.ixidi"
    version = "1.0"
}

subprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "com.github.johnrengelman.shadow")

    repositories {
        jcenter()
        mavenCentral()
    }

}
