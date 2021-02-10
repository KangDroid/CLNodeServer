package com.kangdroid.node.api

import com.kangdroid.node.component.DeviceInfo
import com.kangdroid.node.data.dto.AliveResponseDto
import com.kangdroid.node.data.dto.ImageResponseDto
import com.kangdroid.node.service.SystemExecutorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.util.SocketUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DeviceApiController {

    @Autowired
    private lateinit var deviceInfo: DeviceInfo

    @Autowired
    private lateinit var systemExecutorService: SystemExecutorService

    @GetMapping("/api/alive")
    fun getServerAlive(): AliveResponseDto {
        return systemExecutorService.isServerAlive()
    }

    @GetMapping("/api/node/load")
    fun getLoad(): String = deviceInfo.getLoadPercentage()

    @GetMapping("/api/node/port")
    fun getAvailPort(): String {
        return SocketUtils.findAvailableTcpPort().toString()
    }

    @PostMapping("/api/node/image")
    fun createImage(): ImageResponseDto {
        return systemExecutorService.createImage()
    }
}