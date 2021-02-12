package com.kangdroid.node.data.dto.docker

import com.fasterxml.jackson.annotation.JsonProperty

class CreateResponseDto(
        @JsonProperty("Id")
        var Id: String,

        @JsonProperty("Warnings")
        var Warnings: Array<String>
)