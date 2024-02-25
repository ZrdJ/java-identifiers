package com.github.zrdj.java.identifiers.codecs;

import com.github.f4b6a3.ulid.Ulid;
import com.github.zrdj.java.identifiers.Codec;

public enum UlidCodec implements Codec<Ulid> {
    Base32Crockford(new Codec<>() {
        @Override
        public String encode(Ulid identifier) {
            return identifier.toString();
        }

        @Override
        public Ulid decode(String text) {
            return Ulid.from(text);
        }
    }),
    ;
    private final Codec<Ulid> _codec;

    UlidCodec(final Codec<Ulid> codec) {
        _codec = codec;
    }

    @Override
    public String encode(final Ulid uuid) {
        return _codec.encode(uuid);
    }

    @Override
    public Ulid decode(final String value) {
        return _codec.decode(value);
    }

    public boolean isValid(final String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        try {
            decode(value);
            return true;
        } catch (IllegalArgumentException uce) {
            return false;
        }
    }

}