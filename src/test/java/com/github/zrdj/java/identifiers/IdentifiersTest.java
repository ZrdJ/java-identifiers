package com.github.zrdj.java.identifiers;

import com.github.f4b6a3.ksuid.Ksuid;
import com.github.f4b6a3.tsid.Tsid;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.uuid.enums.UuidLocalDomain;
import com.github.f4b6a3.uuid.enums.UuidNamespace;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers `identifier-generation`: {@code Identifiers.*} must delegate to the matching f4b6a3
 * creator method. A delegation cannot be observed directly, but each UUID version stamps its
 * own version nibble into the value regardless of which factory produced it — so asserting
 * {@code UUID.version()} is enough to catch the case that matters: two methods swapped.
 */
public class IdentifiersTest {

    // [impl->req~identifier-generation.uuid-parameterless~1]
    @Test
    public void uuidV1IsTimeBased() {
        final UUID id = Identifiers.UUIDv1();

        assertThat(id.version()).isEqualTo(1);
    }

    // [impl->req~identifier-generation.uuid-parameterless~1]
    @Test
    public void uuidV4IsRandomBased() {
        final UUID id = Identifiers.UUIDv4();

        assertThat(id.version()).isEqualTo(4);
    }

    // [impl->req~identifier-generation.uuid-parameterless~1]
    @Test
    public void uuidV6IsTimeOrdered() {
        final UUID id = Identifiers.UUIDv6();

        assertThat(id.version()).isEqualTo(6);
    }

    // [impl->req~identifier-generation.uuid-parameterless~1]
    @Test
    public void uuidV7IsTimeOrderedEpoch() {
        final UUID id = Identifiers.UUIDv7();

        assertThat(id.version()).isEqualTo(7);
    }

    // [impl->req~identifier-generation.uuid-parameterized~1]
    @Test
    public void uuidV2IsDceSecurity() {
        final UUID id = Identifiers.UUIDv2(UuidLocalDomain.LOCAL_DOMAIN_PERSON, 1000);

        assertThat(id.version()).isEqualTo(2);
    }

    // [impl->req~identifier-generation.uuid-parameterized~1]
    @Test
    public void uuidV3IsNameBasedMd5() {
        final UUID id = Identifiers.UUIDv3(UuidNamespace.NAMESPACE_DNS, "zrdj.org");

        assertThat(id.version()).isEqualTo(3);
    }

    // [impl->req~identifier-generation.uuid-parameterized~1]
    @Test
    public void uuidV5IsNameBasedSha1() {
        final UUID id = Identifiers.UUIDv5(UuidNamespace.NAMESPACE_DNS, "zrdj.org");

        assertThat(id.version()).isEqualTo(5);
    }

    // [impl->req~identifier-generation.ulid-tsid-ksuid~1]
    @Test
    public void ulidIsFreshOnEachCall() {
        final Ulid a = Identifiers.Ulid();
        final Ulid b = Identifiers.Ulid();

        assertThat(a).isNotNull();
        assertThat(a).isNotEqualTo(b);
    }

    // [impl->req~identifier-generation.ulid-tsid-ksuid~1]
    @Test
    public void tsidIsFreshOnEachCall() {
        final Tsid a = Identifiers.Tsid();
        final Tsid b = Identifiers.Tsid();

        assertThat(a).isNotNull();
        assertThat(a).isNotEqualTo(b);
    }

    // [impl->req~identifier-generation.ulid-tsid-ksuid~1]
    @Test
    public void ksuidIsFreshOnEachCall() {
        final Ksuid a = Identifiers.Ksuid();
        final Ksuid b = Identifiers.Ksuid();

        assertThat(a).isNotNull();
        assertThat(a).isNotEqualTo(b);
    }
}
