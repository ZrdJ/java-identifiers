package com.github.zrdj.java.identifiers.codecs;

import com.github.f4b6a3.uuid.codec.UuidCodec;
import com.github.f4b6a3.uuid.codec.base.*;
import com.github.zrdj.java.identifiers.Codec;

import java.util.UUID;

public enum UUIDCodec implements Codec<UUID> {
    Base16(Base16Codec.INSTANCE),
    Base32(Base32Codec.INSTANCE),
    Base62(Base62Codec.INSTANCE),
    Base64(Base64Codec.INSTANCE),
    Base64Url(Base64UrlCodec.INSTANCE),
    ;

    private final UuidCodec<String> _codec;


    UUIDCodec(final UuidCodec<String> codec) {
        _codec = codec;
    }

    @Override
    public String encode(final UUID uuid) {
        return _codec.encode(uuid);
    }

    @Override
    public UUID decode(final String value) {
        return _codec.decode(value);
    }

    public boolean isValid(final String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            decode(value);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

}
