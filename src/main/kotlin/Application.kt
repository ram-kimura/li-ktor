package com.example

import com.example.util.jsonSerializer
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain
        .main(args)
}

fun Application.module() {
    val env = environment.config.propertyOrNull("ktor.environment")?.getString() ?: "development"
    val logger = LoggerFactory.getLogger(this::class.java)
    logger.info("Environment: $env")

    installPlugins()
    configureRouting()
}

fun Application.installPlugins() {
    install(Resources)

    install(ContentNegotiation) {
        json(jsonSerializer)
    }

    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respondText("App in illegal state as ${cause.message}")
        }
    }

    install(CallLogging)
}
