package com.example.util

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

val config by lazy {
    HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/li?currentSchema=li"
        username = "postgres"
        password = "example"
        maximumPoolSize = 3
    }
}

val dataSource by lazy {
    HikariDataSource(config)
}
