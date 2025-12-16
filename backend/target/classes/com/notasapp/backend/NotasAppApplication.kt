package com.notasapp.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NotasAppApplication

fun main(args: Array<String>) {
    runApplication<NotasAppApplication>(*args)
}
