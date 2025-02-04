package com.example.util

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.statement.Slf4JSqlLogger

val config by lazy {
    HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/li?currentSchema=li"
        username = "postgres"
        password = "example"
        maximumPoolSize = 10
        minimumIdle = 5
        idleTimeout = 10000
    }
}

val dataSource by lazy {
    HikariDataSource(config)
}

val jdbi by lazy {
    Jdbi.create(dataSource).apply {
        installPlugins()
        setSqlLogger(Slf4JSqlLogger())
    }
}
