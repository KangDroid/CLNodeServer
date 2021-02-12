package com.kangdroid.node.data.dto.docker

import com.fasterxml.jackson.annotation.JsonProperty

class PortBindings(
        @get:JsonProperty("22/tcp")
        var sshForward: List<HostInnerBindings>
)