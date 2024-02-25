package com.github.zrdj.java.identifiers;

import com.github.f4b6a3.ksuid.Ksuid;
import com.github.f4b6a3.tsid.Tsid;
import com.github.f4b6a3.ulid.Ulid;
import com.github.zrdj.java.identifiers.codecs.KsuidCodec;
import com.github.zrdj.java.identifiers.codecs.TsidCodec;
import com.github.zrdj.java.identifiers.codecs.UUIDCodec;
import com.github.zrdj.java.identifiers.codecs.UlidCodec;

import java.util.UUID;

public interface Codecs {
    static Codec<UUID> UUIDBase32() {
        return UUIDCodec.Base32;
    }

    static Codec<Ulid> UlidBase32() {
        return UlidCodec.Base32Crockford;
    }

    static Codec<Tsid> TsidBase32() {
        return TsidCodec.Base32Crockford;
    }

    static Codec<Ksuid> KsuidBase62() {
        return KsuidCodec.Base62;
    }

    static Codec<UUID> UUIDBase16() {
        return UUIDCodec.Base16;
    }

    static Codec<UUID> UUIDBase62() {
        return UUIDCodec.Base62;
    }

    static Codec<UUID> UUIDBase64() {
        return UUIDCodec.Base64;
    }

    static Codec<UUID> UUIDBase64Url() {
        return UUIDCodec.Base64Url;
    }

}
