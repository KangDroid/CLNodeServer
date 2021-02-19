package com.kangdroid.node.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
@ConfigurationProperties("kdr")
class DockerConfigurationComponent {
    lateinit var serverIp: String
    lateinit var serverPort: String
    lateinit var dockerEngineVersion: String
    var serverFinalAddress: String = ""

    @PostConstruct
    fun initServerIpAddress() {
        serverFinalAddress = "http://$serverIp:$serverPort/$dockerEngineVersion"
    }
}