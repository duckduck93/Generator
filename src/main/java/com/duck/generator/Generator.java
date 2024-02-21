package com.duck.generator;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sdhan
 * @since 2024.02.21
 */
public class Generator {

    private static final int DATA_CENTER_ID_BITS = 4;
    private static final int SERVER_ID_BITS = 4;
    private static final int SEQUENCE_BITS = 12;

    private static final int MAX_DATA_CENTER_ID = (int)Math.pow(2, DATA_CENTER_ID_BITS) - 1;
    private static final int MAX_SERVER_ID = (int)Math.pow(2, SERVER_ID_BITS) - 1;
    private static final int MAX_SEQUENCE = (int)Math.pow(2, SEQUENCE_BITS) - 1;

    private static final int TIMESTAMP_ID_SHIFT = DATA_CENTER_ID_BITS + SERVER_ID_BITS + SEQUENCE_BITS;
    private static final int DATA_CENTER_ID_SHIFT = SERVER_ID_BITS + SEQUENCE_BITS;
    private static final int SERVER_ID_SHIFT = SEQUENCE_BITS;

    private final AtomicInteger sequence = new AtomicInteger(0);
    private long lastTimestamp = 0L;

    private final long epoch;
    private final long dataCenterId;
    private final long serverId;

    public Generator(long epoch, long dataCenterId, long serverId) {
        this.epoch = epoch;
        this.dataCenterId = dataCenterId;
        this.serverId = serverId;
    }

    public long getId() {
        long timestamp = Instant.now().toEpochMilli() - epoch;

        int seq = getSequence(timestamp);
        if (seq > MAX_SEQUENCE)
            throw new SequenceOverflowException();

        return (timestamp << TIMESTAMP_ID_SHIFT) |
            (dataCenterId << DATA_CENTER_ID_SHIFT) |
            serverId << SERVER_ID_SHIFT |
            seq;
    }

    private int getSequence(long timestamp) {
        if (lastTimestamp == timestamp) {
            return sequence.incrementAndGet();
        }

        lastTimestamp = timestamp;
        sequence.set(0);
        return 0;
    }

    public long findTimestamp(long id) {
        return (id >> TIMESTAMP_ID_SHIFT) + epoch;
    }

    public int findDataCenterId(long id) {
        return (int)(id >> DATA_CENTER_ID_SHIFT) & MAX_DATA_CENTER_ID;
    }

    public int findServerId(long id) {
        return (int)(id >> SERVER_ID_SHIFT) & MAX_SERVER_ID;
    }

    public int findSequence(long id) {
        return (int)id & MAX_SEQUENCE;
    }

}
