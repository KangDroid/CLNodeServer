package com.kangdroid.node.component

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest
@RunWith(SpringRunner::class)
class DeviceInfoTest {

    @Autowired
    private lateinit var deviceInfo: DeviceInfo

    @Test
    fun isLoadGettingWell() {
        // do work
        val outputString: String = deviceInfo.getLoadPercentage()

        // assert!
        assertThat(outputString.length).isGreaterThan(1)
    }
}