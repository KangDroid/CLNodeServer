package com.kangdroid.node.service

import com.kangdroid.node.data.dto.AliveResponseDto
import com.kangdroid.node.data.dto.ImageResponseDto
import org.springframework.stereotype.Service
import org.springframework.util.SocketUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

@Service
class SystemExecutorService {
    fun isServerAlive(): AliveResponseDto {
        val response: AliveResponseDto = AliveResponseDto()

        response.isDockerServerRunning = isDockerServiceRunning().also {
            response.errorMessage = if (!it) {
                "Dockerd Service[Backend Docker Engine] is Not Running on node System!"
            } else {
                ""
            }
        }

        return response
    }

    fun createImage(): ImageResponseDto {
        val availPort: String = "${SocketUtils.findAvailableTcpPort()}"
        val command: Array<String> = arrayOf(
                "docker",
                "run",
                "-p",
                "$availPort:22",
                "-dit",
                "ubuntu:testssh"
        )
        val process: Process = Runtime.getRuntime().exec(command)

        val stdOut: BufferedReader = BufferedReader(InputStreamReader(process.inputStream))

        println("The STDOUT")
        var s: String? = null
        var hash: String = "Error"
        while (true) {
            s = stdOut.readLine()
            println(s)
            if (s == null) break
            hash = s
        }

        var errorMessage: String = ""
        val ipAddress: String = getSystemIPAddress() ?: run {
            errorMessage += "Cannot find IP Address of Compute Node."
            ""
        }

        return ImageResponseDto(
                targetIpAddress = ipAddress,
                targetPort = availPort,
                containerId = hash,
                errorMessage = errorMessage
        )
    }

    private fun isDockerServiceRunning(): Boolean {
        val command: Array<String> = arrayOf(
                "pidof",
                "dockerd"
        )

        val process: Process = Runtime.getRuntime().exec(command)

        val stdOut: BufferedReader = BufferedReader(InputStreamReader(process.inputStream))
        var s: String?
        var pidOutput: String = "Error"
        while (true) {
            s = stdOut.readLine()
            println(s)
            if (s == null) break
            pidOutput = s
        }

        return (pidOutput != "Error")
    }

    private fun getSystemIPAddress(): String? {
        try {
            val enumeratedNetworkInterface: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (enumeratedNetworkInterface.hasMoreElements()) {
                val networkInterface: NetworkInterface = enumeratedNetworkInterface.nextElement()
                val enumeratedInetAddress: Enumeration<InetAddress> = networkInterface.inetAddresses

                // If interface contains "docker", skip it..
                if (networkInterface.displayName.contains("docker")) {
                    continue;
                }

                while (enumeratedInetAddress.hasMoreElements()) {
                    val inetAddress: InetAddress = enumeratedInetAddress.nextElement()

                    // Check whether it is NOT loopback, LAN!
                    if (!inetAddress.isLoopbackAddress && !inetAddress.isLinkLocalAddress && inetAddress.isSiteLocalAddress) {
                        return inetAddress.hostAddress.toString()
                    }
                }
            }
        } catch (ex: SocketException) {
            return null
        }
        return null
    }
}