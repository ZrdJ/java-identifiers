package com.github.zrdj.java.identifiers;

public interface Codec<ID> {
    String encode(ID identifier);

    ID decode(String type);
}
