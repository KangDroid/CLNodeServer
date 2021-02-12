package com.kangdroid.node.data.dto.docker

import com.fasterxml.jackson.annotation.JsonProperty

class HostInnerBindings(
        @get:JsonProperty("HostIp")
        var hostIp: String,

        @get:JsonProperty("HostPort")
        var hostPort: String,
)

class PortBindings(
        @get:JsonProperty("22/tcp")
        var sshForward: List<HostInnerBindings>
)

class HostConfig(
        @get:JsonProperty("PortBindings")
        var portBindings: PortBindings
)

class MainRequest(
        var Image: String,

        @get:JsonProperty("OpenStdin")
        var OpenStdin: Boolean,

        @get:JsonProperty("Tty")
        var Tty: Boolean,

        @get:JsonProperty("HostConfig")
        var hostConfig: HostConfig

)