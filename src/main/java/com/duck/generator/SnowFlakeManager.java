package com.duck.generator;

import lombok.RequiredArgsConstructor;

/**
 * @author sdhan
 * @since 2024.02.27
 */
@RequiredArgsConstructor(staticName = "initialize")
public class SnowFlakeManager {

    private final SnowFlakeConfiguration configuration;

    public long findTimestamp(Id id) {
        return (id.value() >> configuration.getTimestampShift()) + configuration.getTimestampEpoch();
    }

    public int findDataCenterId(Id id) {
        return (int)(id.value() >> configuration.getDatacenterIdShift()) & configuration.getMaxDatacenterId();
    }

    public int findServerId(Id id) {
        return (int)(id.value() >> configuration.getServerIdShift()) & configuration.getMaxServerId();
    }

    public int findSequence(Id id) {
        return (int)id.value() & configuration.getMaxSequence();
    }

}
