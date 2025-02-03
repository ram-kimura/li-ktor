package com.example.util

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jdbi.v3.core.Jdbi

val config by lazy {
    HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/li?currentSchema=li"
        username = "postgres"
        password = "example"
        maximumPoolSize = 3
        minimumIdle = 1
        idleTimeout = 10000
    }
}

val dataSource by lazy {
    HikariDataSource(config)
}

val jdbi by lazy {
    Jdbi.create(dataSource).apply {
        installPlugins()
    }
}
