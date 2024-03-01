package com.duck.generator;

import lombok.*;

/**
 * @author sdhan
 * @since 2024.02.27
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SnowFlakeConfiguration {

    @Builder.Default
    private final int timestampBits = 48;
    @Builder.Default
    private final int datacenterIdBits = 4;
    @Builder.Default
    private final int serverIdBits = 4;
    @Builder.Default
    private final int sequenceBits = 8;

    @Builder.Default
    private final long timestampEpoch = 0;
    @Builder.Default
    private final int datacenterId = 0;
    @Builder.Default
    private final int serverId = 0;

    private Integer timestampShift;
    private Integer datacenterIdShift;
    private Integer serverIdShift;

    private Integer maxDatacenterId;
    private Integer maxServerId;
    private Integer maxSequence;

    public int getTimestampShift() {
        if (timestampShift == null) {
            timestampShift = datacenterIdBits + serverIdBits + sequenceBits;
        }
        return timestampShift;
    }

    public int getDatacenterIdShift() {
        if (datacenterIdShift == null) {
            datacenterIdShift = serverIdBits + sequenceBits;
        }
        return datacenterIdShift;
    }

    public int getServerIdShift() {
        return sequenceBits;
    }

    public int getMaxDatacenterId() {
        if (maxDatacenterId == null) {
            maxDatacenterId = (int)Math.pow(2, datacenterIdBits) - 1;
        }
        return maxDatacenterId;
    }

    public int getMaxServerId() {
        if (maxServerId == null) {
            maxServerId = (int)(Math.pow(2, serverIdBits) - 1);
        }
        return maxServerId;
    }

    public int getMaxSequence() {
        if (maxSequence == null) {
            maxSequence = (int)(Math.pow(2, sequenceBits) - 1);
        }
        return maxSequence;
    }

}
