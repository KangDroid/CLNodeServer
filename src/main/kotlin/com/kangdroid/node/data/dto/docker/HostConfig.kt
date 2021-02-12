package com.kangdroid.node.data.dto.docker

import com.fasterxml.jackson.annotation.JsonProperty

class HostConfig(
        @get:JsonProperty("PortBindings")
        var portBindings: PortBindings
)