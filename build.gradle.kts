import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.10"
    id("org.jetbrains.kotlin.kapt") version "2.0.20"
    application
}

group = "me.red_byte"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { setUrl("https://dl.bintray.com/arrow-kt/arrow-kt/") }
}

dependencies {
    implementation("io.arrow-kt:arrow-core:1.2.4")
    kapt("io.arrow-kt:arrow-meta:0.11.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

tasks.test {
    useJUnitPlatform()
}
//tasks.withType<KotlinCompile> {
//    kotlinOptions.jvmTarget = "1.8"
//}

