package com.kangdroid.node.data.dto

/**
 * ImageResponseDto: When createContainer called, get all of those information and return them.
 *
 * variables:
 * targetIpAddress for SSH-IPAddress[or region ip address actually]
 * targetPort for SSH-Access Port[its not 22]
 * containerId for Docker Container Id
 */
class ImageResponseDto(
        var targetIpAddress: String,
        var targetPort: String,
        var containerId: String,
        var regionLocation: String = "",
        var errorMessage: String = "",
)