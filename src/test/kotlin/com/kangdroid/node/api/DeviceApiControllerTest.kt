package com.kangdroid.node.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
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
}