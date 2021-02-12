package com.kangdroid.node.data.dto.docker

import com.fasterxml.jackson.annotation.JsonProperty

class MainRequest(
        @get:JsonProperty("Image")
        var image: String,

        @get:JsonProperty("OpenStdin")
        var openStdin: Boolean,

        @get:JsonProperty("Tty")
        var tty: Boolean,

        @get:JsonProperty("HostConfig")
        var hostConfig: HostConfig
)