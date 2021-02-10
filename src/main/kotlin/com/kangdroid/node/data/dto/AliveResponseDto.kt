package com.kangdroid.node.data.dto

/**
 * Returns whether server is alive.
 */
class AliveResponseDto(
        var isDockerServerRunning: Boolean = false,
        var errorMessage: String = ""
)