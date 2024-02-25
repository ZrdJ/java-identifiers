package com.github.zrdj.java.identifiers;

import java.util.UUID;

public class main {
    public static void main(String[] args) {
        final UUID id = Identifiers.UUIDv7();
        final Codec<UUID> base32 = Codecs.UUIDBase32();
        System.out.println("id as base32: " + base32.encode(id));
    }
}
