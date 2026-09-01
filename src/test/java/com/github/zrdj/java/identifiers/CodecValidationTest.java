package com.github.zrdj.java.identifiers;

import com.github.zrdj.java.identifiers.codecs.KsuidCodec;
import com.github.zrdj.java.identifiers.codecs.TsidCodec;
import com.github.zrdj.java.identifiers.codecs.UUIDCodec;
import com.github.zrdj.java.identifiers.codecs.UlidCodec;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Covers `identifier-text-codec.text-validation`: {@code isValid(String)} on each concrete
 * codec enum (declared there, not on {@code Codec<ID>} — see the requirement text).
 */
public class CodecValidationTest {

    // [impl->req~identifier-text-codec.text-validation~1]
    @Test
    public void validTextIsValid() {
        assertThat(UUIDCodec.Base32.isValid(UUIDCodec.Base32.encode(Identifiers.UUIDv4()))).isTrue();
        assertThat(UlidCodec.Base32Crockford.isValid(UlidCodec.Base32Crockford.encode(Identifiers.Ulid()))).isTrue();
        assertThat(TsidCodec.Base32Crockford.isValid(TsidCodec.Base32Crockford.encode(Identifiers.Tsid()))).isTrue();
        assertThat(KsuidCodec.Base62.isValid(KsuidCodec.Base62.encode(Identifiers.Ksuid()))).isTrue();
    }

    // [impl->req~identifier-text-codec.text-validation~1]
    @Test
    public void textThatFailsToDecodeIsInvalid() {
        // isValid must not throw for text that decode() would reject -- the assertions
        // below fail with the codec's exception, not with `false`, if that promise breaks.
        assertThat(UUIDCodec.Base32.isValid("not-a-valid-uuid")).isFalse();
        assertThat(UlidCodec.Base32Crockford.isValid("not-a-valid-ulid")).isFalse();
        assertThat(TsidCodec.Base32Crockford.isValid("not-a-valid-tsid")).isFalse();
        assertThat(KsuidCodec.Base62.isValid("not-a-valid-ksuid")).isFalse();
    }

    // [impl->req~identifier-text-codec.text-validation~1]
    @Test
    public void nullOrBlankTextIsInvalid() {
        assertThat(UUIDCodec.Base32.isValid(null)).isFalse();
        assertThat(UUIDCodec.Base32.isValid("")).isFalse();
        assertThat(UUIDCodec.Base32.isValid("   ")).isFalse();

        assertThat(UlidCodec.Base32Crockford.isValid(null)).isFalse();
        assertThat(UlidCodec.Base32Crockford.isValid("")).isFalse();
        assertThat(UlidCodec.Base32Crockford.isValid("   ")).isFalse();

        assertThat(TsidCodec.Base32Crockford.isValid(null)).isFalse();
        assertThat(TsidCodec.Base32Crockford.isValid("")).isFalse();
        assertThat(TsidCodec.Base32Crockford.isValid("   ")).isFalse();

        assertThat(KsuidCodec.Base62.isValid(null)).isFalse();
        assertThat(KsuidCodec.Base62.isValid("")).isFalse();
        assertThat(KsuidCodec.Base62.isValid("   ")).isFalse();
    }
}
