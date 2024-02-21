package com.duck.generator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

/**
 * @author sdhan
 * @since 2024.02.21
 */
class GeneratorTests {

    private final long DATA_CENTER_ID = 1;
    private final long SERVER_ID = 1;
    private final Generator generator = new Generator(0L, DATA_CENTER_ID, SERVER_ID);

    @Test
    @DisplayName("64bit ID 생성 테스트")
    void testGenerator() {
        long timestamp = 1704067200000L;

        Clock clock = Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneId.of("UTC"));
        Instant instant = Instant.now(clock);

        try (MockedStatic<Instant> mockedStatic = Mockito.mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            long id1 = generator.getId();
            Assertions.assertThat(generator.findTimestamp(id1)).isEqualTo(timestamp);
            Assertions.assertThat(generator.findDataCenterId(id1)).isEqualTo(DATA_CENTER_ID);
            Assertions.assertThat(generator.findServerId(id1)).isEqualTo(SERVER_ID);
            Assertions.assertThat(generator.findSequence(id1)).isZero();

            // 두 번째 ID 생성
            long id2 = generator.getId();
            Assertions.assertThat(generator.findTimestamp(id2)).isEqualTo(timestamp);
            Assertions.assertThat(generator.findDataCenterId(id2)).isEqualTo(DATA_CENTER_ID);
            Assertions.assertThat(generator.findServerId(id2)).isEqualTo(SERVER_ID);
            Assertions.assertThat(generator.findSequence(id2)).isOne();
        }

    }

    @Test
    @DisplayName("64bit ID 생성 Unique 테스트")
    void testUniqueIdsGenerated() {
        int GENERATE_SIZE = 10_000;
        Set<Long> idSet = new HashSet<>();
        for (int i = 0; i < GENERATE_SIZE; i++) {
            idSet.add(generator.getId());
        }

        Assertions.assertThat(idSet).hasSize(GENERATE_SIZE);
    }

    @Test
    @DisplayName("64bit ID 생성 성능 테스트")
    void testGenerationTime() {
        int THRESHOLD = 4_096;

        long s = System.currentTimeMillis();
        for (int i = 0; i < THRESHOLD; i++) {
            generator.getId();
        }
        long e = System.currentTimeMillis();
        System.out.println(e - s);
        Assertions.assertThat(e - s).isLessThanOrEqualTo(100);
    }

}
