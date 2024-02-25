package com.github.zrdj.java.identifiers.codecs;

import com.github.f4b6a3.tsid.Tsid;
import com.github.zrdj.java.identifiers.Codec;

public enum TsidCodec implements Codec<Tsid> {
    Base32Crockford(new Codec<>() {
        @Override
        public String encode(Tsid identifier) {
            return identifier.toString();
        }

        @Override
        public Tsid decode(String text) {
            return Tsid.from(text);
        }
    }),
    ;
    private final Codec<Tsid> _codec;

    TsidCodec(final Codec<Tsid> codec) {
        _codec = codec;
    }

    @Override
    public String encode(final Tsid uuid) {
        return _codec.encode(uuid);
    }

    @Override
    public Tsid decode(final String value) {
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