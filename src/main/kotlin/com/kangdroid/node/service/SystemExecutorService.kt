package com.kangdroid.node.service

import com.kangdroid.node.configuration.DockerConfigurationComponent
import com.kangdroid.node.data.dto.AliveResponseDto
import com.kangdroid.node.data.dto.ImageResponseDto
import com.kangdroid.node.data.dto.RestartContainerRequestDto
import com.kangdroid.node.data.dto.docker.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.util.SocketUtils
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

@Service
class SystemExecutorService {
    @Autowired
    private lateinit var dockerConfigurationComponent: DockerConfigurationComponent
    private val restTemplate: RestTemplate = RestTemplate()

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
        val createUrl: String = "${dockerConfigurationComponent.serverFinalAddress}/containers/create"
        val availPort: String = SocketUtils.findAvailableTcpPort().toString()
        val requestDocker: MainRequest = MainRequest(
                image = "ubuntu:testssh",
                openStdin = true,
                tty = true,
                hostConfig = HostConfig(
                        PortBindings(
                                listOf(HostInnerBindings(
                                        hostIp = "",
                                        hostPort = availPort
                                ))
                        )
                )
        )

        // CreateImage
        val createResponse: ResponseEntity<CreateResponseDto> =
                restTemplate.postForEntity(createUrl, requestDocker, CreateResponseDto::class.java)
        if (createResponse.statusCode != HttpStatus.CREATED) {
            return ImageResponseDto(
                    errorMessage = "Image Creation - Server responded with: ${createResponse.statusCode}"
            )
        }

        val creationResponseBody: CreateResponseDto = createResponse.body ?: return ImageResponseDto(
                errorMessage = "Image Creation - Server did not responded with body."
        )

        // Image Start Service
        val startUrl: String = "${dockerConfigurationComponent.serverFinalAddress}/containers/${creationResponseBody.Id}/start"
        val startResponseBody: ResponseEntity<Void> =
                restTemplate.postForEntity(startUrl, null, Void::class.java)

        if (startResponseBody.statusCode != HttpStatus.NO_CONTENT) {
            return ImageResponseDto(
                    errorMessage = "Image Start - Image Creation succeed, but Image Start responded with: ${startResponseBody.statusCode}"
            )
        }

        val ipAddress: String = getSystemIPAddress() ?: return ImageResponseDto(
                errorMessage = "Cannot find IP Address of Compute Node."
        )

        return ImageResponseDto(
                targetIpAddress = ipAddress,
                targetPort = availPort,
                containerId = creationResponseBody.Id,
                errorMessage = ""
        )
    }

    fun restartContainer(restartContainerRequestDto: RestartContainerRequestDto): String {
        val restartUrl: String = "${dockerConfigurationComponent.serverFinalAddress}/containers/${restartContainerRequestDto.containerId}/restart"
        val responseEntity: ResponseEntity<String> = restTemplate.postForEntity(restartUrl, null ,String::class)
        return if (responseEntity.statusCode != HttpStatus.NO_CONTENT) {
            "Something went wrong when communicating docker engine!"
        } else {
            ""
        }
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