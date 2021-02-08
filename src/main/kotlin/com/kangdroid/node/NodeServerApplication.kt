package com.kangdroid.node

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NodeServerApplication

fun main(args: Array<String>) {
	runApplication<NodeServerApplication>(*args)
}
