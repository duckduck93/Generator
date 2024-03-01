package com.duck.generator;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sdhan
 * @since 2024.02.21
 */
@RequiredArgsConstructor(staticName = "initialize")
public class SnowFlakeGenerator implements Generator {

    private final SnowFlakeConfiguration configuration;

    private final AtomicInteger sequence = new AtomicInteger(0);
    private long lastTimestamp = 0L;

    public Id generateId() {
        long timestampBit = getTimestampBit(Instant.now().toEpochMilli() - configuration.getTimestampEpoch());

        int sequenceBit = getSequenceBit(timestampBit);
        return Id.from(timestampBit
                           | getDatacenterIdBit(configuration.getDatacenterId())
                           | getServerIdBit(configuration.getServerId())
                           | sequenceBit);
    }

    private int getSequenceBit(long timestamp) {
        if (lastTimestamp != timestamp) {
            lastTimestamp = timestamp;
            sequence.set(0);
            return 0;
        }

        return sequence.incrementAndGet();
    }

    private long getTimestampBit(long timestamp) {
        return timestamp << (
            configuration.getDatacenterIdBits()
                + configuration.getServerIdBits()
                + configuration.getSequenceBits()
        );
    }

    private long getDatacenterIdBit(long datacenterId) {
        return datacenterId << (
            configuration.getServerIdBits()
                + configuration.getSequenceBits()
        );
    }

    private long getServerIdBit(long serverId) {
        return serverId << (
            configuration.getSequenceBits()
        );
    }

}
