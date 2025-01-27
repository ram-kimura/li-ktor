import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.server.call.logging)
    implementation(libs.ktor.serialization)
    implementation(libs.kotlin.result)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.assertj.core)
}

tasks.test {
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
    }

    useJUnitPlatform {
        includeEngines = setOf("junit-jupiter")
    }
}
