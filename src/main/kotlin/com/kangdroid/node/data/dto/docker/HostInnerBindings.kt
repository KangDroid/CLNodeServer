package com.kangdroid.node.data.dto.docker

import com.fasterxml.jackson.annotation.JsonProperty

class HostInnerBindings(
        @get:JsonProperty("HostIp")
        var hostIp: String,

        @get:JsonProperty("HostPort")
        var hostPort: String
)