package com.slack.kaldb.metadata.cache;

import static com.slack.kaldb.proto.metadata.Metadata.IndexType.LOGS_LUCENE9;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.slack.kaldb.proto.metadata.Metadata;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CacheSlotMetadataTest {

  private static final List<Metadata.IndexType> SUPPORTED_INDEX_TYPES = List.of(LOGS_LUCENE9);

  @Test
  public void testCacheSlotMetadata() {
    String hostname = "hostname";
    String name = "name";
    Metadata.CacheSlotMetadata.CacheSlotState cacheSlotState =
        Metadata.CacheSlotMetadata.CacheSlotState.FREE;
    String replicaId = "";
    long updatedTimeEpochMs = Instant.now().toEpochMilli();

    CacheSlotMetadata cacheSlotMetadata =
        new CacheSlotMetadata(
            name, cacheSlotState, replicaId, updatedTimeEpochMs, SUPPORTED_INDEX_TYPES, hostname);
    assertThat(cacheSlotMetadata.name).isEqualTo(name);
    assertThat(cacheSlotMetadata.hostname).isEqualTo(hostname);
    assertThat(cacheSlotMetadata.cacheSlotState).isEqualTo(cacheSlotState);
    assertThat(cacheSlotMetadata.replicaId).isEqualTo(replicaId);
    assertThat(cacheSlotMetadata.updatedTimeEpochMs).isEqualTo(updatedTimeEpochMs);
    assertThat(cacheSlotMetadata.supportedIndexTypes).isEqualTo(SUPPORTED_INDEX_TYPES);
  }

  @Test
  public void testCacheSlotEqualsHashcode() {
    String hostname = "hostname";
    String name = "name";
    Metadata.CacheSlotMetadata.CacheSlotState cacheSlotState =
        Metadata.CacheSlotMetadata.CacheSlotState.ASSIGNED;
    String replicaId = "123";
    long updatedTimeEpochMs = Instant.now().toEpochMilli();

    CacheSlotMetadata cacheSlot =
        new CacheSlotMetadata(
            name, cacheSlotState, replicaId, updatedTimeEpochMs, SUPPORTED_INDEX_TYPES, hostname);
    CacheSlotMetadata cacheSlotDuplicate =
        new CacheSlotMetadata(
            name, cacheSlotState, replicaId, updatedTimeEpochMs, SUPPORTED_INDEX_TYPES, hostname);
    CacheSlotMetadata cacheSlotDifferentState =
        new CacheSlotMetadata(
            name,
            Metadata.CacheSlotMetadata.CacheSlotState.EVICT,
            replicaId,
            updatedTimeEpochMs,
            SUPPORTED_INDEX_TYPES,
            hostname);
    CacheSlotMetadata cacheSlotDifferentReplicaId =
        new CacheSlotMetadata(
            name, cacheSlotState, "321", updatedTimeEpochMs, SUPPORTED_INDEX_TYPES, hostname);
    CacheSlotMetadata cacheSlotDifferentUpdatedTime =
        new CacheSlotMetadata(
            name,
            cacheSlotState,
            replicaId,
            updatedTimeEpochMs + 1,
            SUPPORTED_INDEX_TYPES,
            hostname);
    CacheSlotMetadata cacheSlotDifferentSupportedIndexType =
        new CacheSlotMetadata(
            name,
            cacheSlotState,
            replicaId,
            updatedTimeEpochMs + 1,
            List.of(LOGS_LUCENE9, LOGS_LUCENE9),
            hostname);
    CacheSlotMetadata cacheSlotDifferentHostname =
        new CacheSlotMetadata(
            name,
            cacheSlotState,
            replicaId,
            updatedTimeEpochMs,
            SUPPORTED_INDEX_TYPES,
            "hostname2");

    assertThat(cacheSlot.hashCode()).isEqualTo(cacheSlotDuplicate.hashCode());
    assertThat(cacheSlot).isEqualTo(cacheSlotDuplicate);

    assertThat(cacheSlot).isNotEqualTo(cacheSlotDifferentState);
    assertThat(cacheSlot.hashCode()).isNotEqualTo(cacheSlotDifferentState.hashCode());
    assertThat(cacheSlot).isNotEqualTo(cacheSlotDifferentReplicaId);
    assertThat(cacheSlot.hashCode()).isNotEqualTo(cacheSlotDifferentReplicaId.hashCode());
    assertThat(cacheSlot).isNotEqualTo(cacheSlotDifferentUpdatedTime);
    assertThat(cacheSlot).isNotEqualTo(cacheSlotDifferentSupportedIndexType);
    assertThat(cacheSlot.hashCode()).isNotEqualTo(cacheSlotDifferentUpdatedTime.hashCode());
    assertThat(cacheSlot.hashCode()).isNotEqualTo(cacheSlotDifferentSupportedIndexType.hashCode());
    assertThat(cacheSlot).isNotEqualTo(cacheSlotDifferentHostname);
    assertThat(cacheSlot.hashCode()).isNotEqualTo(cacheSlotDifferentHostname.hashCode());
  }

  @Test
  public void invalidArgumentsShouldThrow() {
    assertThatIllegalArgumentException()
        .isThrownBy(
            () ->
                new CacheSlotMetadata(
                    "name",
                    Metadata.CacheSlotMetadata.CacheSlotState.FREE,
                    "123",
                    Instant.now().toEpochMilli(),
                    SUPPORTED_INDEX_TYPES,
                    "hostname"));
    assertThatIllegalArgumentException()
        .isThrownBy(
            () ->
                new CacheSlotMetadata(
                    "name",
                    Metadata.CacheSlotMetadata.CacheSlotState.ASSIGNED,
                    "",
                    Instant.now().toEpochMilli(),
                    SUPPORTED_INDEX_TYPES,
                    "hostname"));
    assertThatIllegalArgumentException()
        .isThrownBy(
            () ->
                new CacheSlotMetadata(
                    "name",
                    Metadata.CacheSlotMetadata.CacheSlotState.EVICT,
                    "",
                    Instant.now().toEpochMilli(),
                    SUPPORTED_INDEX_TYPES,
                    "hostname"));
    assertThatIllegalArgumentException()
        .isThrownBy(
            () ->
                new CacheSlotMetadata(
                    "name",
                    Metadata.CacheSlotMetadata.CacheSlotState.EVICTING,
                    "",
                    Instant.now().toEpochMilli(),
                    SUPPORTED_INDEX_TYPES,
                    "hostname"));
    assertThatIllegalArgumentException()
        .isThrownBy(
            () ->
                new CacheSlotMetadata(
                    "name",
                    Metadata.CacheSlotMetadata.CacheSlotState.LOADING,
                    "",
                    Instant.now().toEpochMilli(),
                    SUPPORTED_INDEX_TYPES,
                    "hostname"));
    assertThatIllegalArgumentException()
        .isThrownBy(
            () ->
                new CacheSlotMetadata(
                    "name",
                    Metadata.CacheSlotMetadata.CacheSlotState.LIVE,
                    "",
                    Instant.now().toEpochMilli(),
                    SUPPORTED_INDEX_TYPES,
                    "hostname"));
    assertThatIllegalArgumentException()
        .isThrownBy(
            () ->
                new CacheSlotMetadata(
                    "name",
                    Metadata.CacheSlotMetadata.CacheSlotState.FREE,
                    "",
                    0,
                    SUPPORTED_INDEX_TYPES,
                    "hostname"));
    assertThatIllegalArgumentException()
        .isThrownBy(
            () ->
                new CacheSlotMetadata(
                    "name",
                    Metadata.CacheSlotMetadata.CacheSlotState.FREE,
                    null,
                    Instant.now().toEpochMilli(),
                    SUPPORTED_INDEX_TYPES,
                    "hostname"));
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> {
              new CacheSlotMetadata(
                  "name",
                  Metadata.CacheSlotMetadata.CacheSlotState.FREE,
                  "123",
                  100000,
                  Collections.emptyList(),
                  "hostname");
            });
    assertThatIllegalArgumentException()
        .isThrownBy(
            () -> {
              new CacheSlotMetadata(
                  "name",
                  Metadata.CacheSlotMetadata.CacheSlotState.FREE,
                  "123",
                  100000,
                  SUPPORTED_INDEX_TYPES,
                  "hostname");
            });
  }
}
