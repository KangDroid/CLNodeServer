package com.kangdroid.node.component

import org.springframework.stereotype.Component
import oshi.SystemInfo
import oshi.hardware.CentralProcessor

@Component
class DeviceInfo {
    private val cpu: CentralProcessor = SystemInfo().hardware.processor

    fun getLoadPercentage(): String {
        val load: DoubleArray = cpu.getSystemLoadAverage(1)
        return String.format("%.2f", load[0])
    }
}