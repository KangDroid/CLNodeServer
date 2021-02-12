package com.kangdroid.node.api

import com.kangdroid.node.data.dto.AliveResponseDto
import com.kangdroid.node.data.dto.docker.HostConfig
import com.kangdroid.node.data.dto.docker.HostInnerBindings
import com.kangdroid.node.data.dto.docker.MainRequest
import com.kangdroid.node.data.dto.docker.PortBindings
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeviceApiControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    private val baseUrl: String = "http://localhost"

    @Test
    fun isGetLoadWorking() {
        // Let
        val url: String = "$baseUrl:$port/api/node/load"
        // Do work
        val tmpResponseEntity: ResponseEntity<String> = testRestTemplate.getForEntity(url, String::class.java)

        // Assert
        assertThat(tmpResponseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(tmpResponseEntity.body).isNotEqualTo(null)
        assertThat(tmpResponseEntity.body?.length).isNotEqualTo(0)
    }

    @Test
    fun isGetPortWorking() {
        // Let
        val url: String = "$baseUrl:$port/api/node/port"
        // Do work
        val tmpResponseEntity: ResponseEntity<String> = testRestTemplate.getForEntity(url, String::class.java)

        // Assert
        assertThat(tmpResponseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(tmpResponseEntity.body).isNotEqualTo(null)
        println("The Avail port: ${tmpResponseEntity.body}")
    }

    @Test
    fun isServerAliveWorkWell() {
        // Let
        val url: String = "$baseUrl:$port/api/alive"
        // Do work
        val tmpResponseEntity: ResponseEntity<AliveResponseDto> = testRestTemplate.getForEntity(url, AliveResponseDto::class.java)

        // Assert
        assertThat(tmpResponseEntity.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(tmpResponseEntity.body).isNotEqualTo(null)

        val aliveResponse: AliveResponseDto = tmpResponseEntity.body!! // Assert above
        assertThat(aliveResponse.errorMessage).isEqualTo("")
    }

    @Test
    fun isTesting() {

        val hostConfig: HostConfig = HostConfig(
                PortBindings(
                        listOf(HostInnerBindings(
                                "",
                                "1200"
                        ))
                )
        )

        val mainRequest: MainRequest = MainRequest(
                Image = "ubuntu:testssh",
                OpenStdin = true,
                Tty = true,
                hostConfig = hostConfig
        )
        class TmpDto(
                var Id: String,
                var Warnings: Array<String>
        )
        val url: String = "http://192.168.0.52:2375/v1.40/containers/create"
        val createResponseEntity: ResponseEntity<TmpDto> = testRestTemplate.postForEntity(url, mainRequest, TmpDto::class.java)
        val dockerId: String = createResponseEntity.body?.Id ?: ""

        val anotherUrl: String = "http://192.168.0.52:2375/v1.40/containers/$dockerId/start"
        val testResponseEntity: ResponseEntity<Void> =
                testRestTemplate.postForEntity(anotherUrl, null, Void::class.java)
    }
}