package com.github.zrdj.java.identifiers.codecs;

import com.github.f4b6a3.ksuid.Ksuid;
import com.github.zrdj.java.identifiers.Codec;

public enum KsuidCodec implements Codec<Ksuid> {
    Base62(new Codec<>() {
        @Override
        public String encode(Ksuid identifier) {
            return identifier.toString();
        }

        @Override
        public Ksuid decode(String text) {
            return Ksuid.from(text);
        }
    }),
    ;
    private final Codec<Ksuid> _codec;

    KsuidCodec(final Codec<Ksuid> codec) {
        _codec = codec;
    }

    @Override
    public String encode(final Ksuid uuid) {
        return _codec.encode(uuid);
    }

    @Override
    public Ksuid decode(final String value) {
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