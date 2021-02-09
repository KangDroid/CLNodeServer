package com.kangdroid.node.api

import com.kangdroid.node.component.DeviceInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.SocketUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DeviceApiController {

    @Autowired
    private lateinit var deviceInfo: DeviceInfo

    @GetMapping("/api/node/load")
    fun getLoad(): String = deviceInfo.getLoadPercentage()

    @GetMapping("/api/node/port")
    fun getAvailPort(): String {
        return SocketUtils.findAvailableTcpPort().toString()
    }
}