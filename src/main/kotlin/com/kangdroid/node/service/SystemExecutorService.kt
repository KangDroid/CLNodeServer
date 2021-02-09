package com.kangdroid.node.service

import org.springframework.stereotype.Service
import org.springframework.util.SocketUtils
import java.io.BufferedReader
import java.io.InputStreamReader

@Service
class SystemExecutorService {
    fun createImage(): String {
        val command: Array<String> = arrayOf(
                "docker",
                "run",
                "-p",
                "${SocketUtils.findAvailableTcpPort()}:22",
                "-dit",
                "ubuntu:testssh"
        )
        val process: Process = Runtime.getRuntime().exec(command)

        val stdOut: BufferedReader = BufferedReader(InputStreamReader(process.inputStream))
        val stdErr: BufferedReader = BufferedReader(InputStreamReader(process.errorStream))

        println("The STDOUT")
        var s: String? = null
        var hash: String = "Error"
        while (true) {
            s = stdOut.readLine()
            if (s == null) break
            hash = s
        }
        return hash
    }
}