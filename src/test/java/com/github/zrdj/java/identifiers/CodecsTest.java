package com.github.zrdj.java.identifiers;

import com.github.f4b6a3.ksuid.Ksuid;
import com.github.f4b6a3.tsid.Tsid;
import com.github.f4b6a3.ulid.Ulid;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers `identifier-text-codec`: encoding and decoding through {@code Codecs.*}.
 */
public class CodecsTest {

    // [impl->req~identifier-text-codec.uuid-formats~1]
    @Test
    public void everyUuidFormatRoundTrips() {
        final UUID id = Identifiers.UUIDv4();
        final List<Codec<UUID>> uuidCodecs = Arrays.asList(
                Codecs.UUIDBase16(), Codecs.UUIDBase32(), Codecs.UUIDBase62(),
                Codecs.UUIDBase64(), Codecs.UUIDBase64Url());

        for (final Codec<UUID> codec : uuidCodecs) {
            final String text = codec.encode(id);
            assertThat(codec.decode(text)).isEqualTo(id);
        }
    }

    // [impl->req~identifier-text-codec.uuid-formats~1]
    @Test
    public void uuidFormatsAreNotInterchangeable() {
        final UUID id = Identifiers.UUIDv4();

        final String base16 = Codecs.UUIDBase16().encode(id);
        final String base32 = Codecs.UUIDBase32().encode(id);

        assertThat(base16).isNotEqualTo(base32);
    }

    // [impl->req~identifier-text-codec.ulid-tsid-ksuid-format~1]
    @Test
    public void ulidRoundTrips() {
        final Ulid id = Identifiers.Ulid();

        final String text = Codecs.UlidBase32().encode(id);

        assertThat(Codecs.UlidBase32().decode(text)).isEqualTo(id);
    }

    // [impl->req~identifier-text-codec.ulid-tsid-ksuid-format~1]
    @Test
    public void tsidRoundTrips() {
        final Tsid id = Identifiers.Tsid();

        final String text = Codecs.TsidBase32().encode(id);

        assertThat(Codecs.TsidBase32().decode(text)).isEqualTo(id);
    }

    // [impl->req~identifier-text-codec.ulid-tsid-ksuid-format~1]
    @Test
    public void ksuidRoundTrips() {
        final Ksuid id = Identifiers.Ksuid();

        final String text = Codecs.KsuidBase62().encode(id);

        assertThat(Codecs.KsuidBase62().decode(text)).isEqualTo(id);
    }
}
