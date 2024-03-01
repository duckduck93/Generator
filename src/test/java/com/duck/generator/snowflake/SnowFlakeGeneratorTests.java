package com.duck.generator.snowflake;

import com.duck.generator.Id;
import com.duck.generator.SnowFlakeConfiguration;
import com.duck.generator.SnowFlakeGenerator;
import com.duck.generator.SnowFlakeManager;
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
class SnowFlakeGeneratorTests {

    @Test
    @DisplayName("64bit ID 생성 테스트")
    void testGenerator() {
        long timestamp = 1704067200000L;
        Clock clock = Clock.fixed(Instant.parse("2024-01-01T00:00:00Z"), ZoneId.of("UTC"));
        Instant instant = Instant.now(clock);
        
        SnowFlakeConfiguration configuration = SnowFlakeConfiguration.builder().build();
        SnowFlakeGenerator generator = SnowFlakeGenerator.initialize(configuration);
        SnowFlakeManager manager = SnowFlakeManager.initialize(configuration);
        
        try (MockedStatic<Instant> mockedStatic = Mockito.mockStatic(Instant.class)) {
            mockedStatic.when(Instant::now).thenReturn(instant);

            Id id1 = generator.generateId();
            Assertions.assertThat(manager.findTimestamp(id1)).isEqualTo(timestamp);
            Assertions.assertThat(manager.findDataCenterId(id1)).isEqualTo(configuration.getDatacenterId());
            Assertions.assertThat(manager.findServerId(id1)).isEqualTo(configuration.getServerId());
            Assertions.assertThat(manager.findSequence(id1)).isZero();

            // 두 번째 ID 생성
            Id id2 = generator.generateId();
            Assertions.assertThat(manager.findTimestamp(id2)).isEqualTo(timestamp);
            Assertions.assertThat(manager.findDataCenterId(id2)).isEqualTo(configuration.getDatacenterId());
            Assertions.assertThat(manager.findServerId(id2)).isEqualTo(configuration.getServerId());
            Assertions.assertThat(manager.findSequence(id2)).isOne();
        }

    }

    @Test
    @DisplayName("64bit ID 생성 Unique 테스트")
    void testUniqueIdsGenerated() {
        SnowFlakeConfiguration configuration = SnowFlakeConfiguration.builder().build();
        SnowFlakeGenerator generator = SnowFlakeGenerator.initialize(configuration);

        int GENERATE_SIZE = 10_000;
        Set<Id> idSet = new HashSet<>();
        for (int i = 0; i < GENERATE_SIZE; i++) {
            idSet.add(generator.generateId());
        }

        Assertions.assertThat(idSet).hasSize(GENERATE_SIZE);
    }

    @Test
    @DisplayName("64bit ID 생성 성능 테스트")
    void testGenerationTime() {
        SnowFlakeConfiguration configuration = SnowFlakeConfiguration.builder().build();
        SnowFlakeGenerator generator = SnowFlakeGenerator.initialize(configuration);

        int THRESHOLD = 4_096;

        long s = System.currentTimeMillis();
        for (int i = 0; i < THRESHOLD; i++) {
            generator.generateId();
        }
        long e = System.currentTimeMillis();
        System.out.println(e - s);
        Assertions.assertThat(e - s).isLessThanOrEqualTo(100);
    }

}
