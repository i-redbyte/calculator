import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.31"
    id("org.jetbrains.kotlin.kapt") version "1.4.31"
    application
}

group = "me.red_byte"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven { setUrl("https://dl.bintray.com/arrow-kt/arrow-kt/") }
}

dependencies {
    implementation("io.arrow-kt:arrow-core:0.11.0")
    implementation("io.arrow-kt:arrow-syntax:0.11.0")
    kapt("io.arrow-kt:arrow-meta:0.11.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testImplementation("com.google.truth:truth:1.1.2")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}