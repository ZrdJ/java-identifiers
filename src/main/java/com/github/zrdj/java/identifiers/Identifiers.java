package com.github.zrdj.java.identifiers;

import com.github.f4b6a3.ksuid.Ksuid;
import com.github.f4b6a3.ksuid.KsuidCreator;
import com.github.f4b6a3.tsid.Tsid;
import com.github.f4b6a3.tsid.TsidCreator;
import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.f4b6a3.uuid.enums.UuidLocalDomain;
import com.github.f4b6a3.uuid.enums.UuidNamespace;

import java.util.UUID;

public interface Identifiers {
    static Ksuid Ksuid() {
        return KsuidCreator.getKsuid();
    }

    static Tsid Tsid() {
        return TsidCreator.getTsid();
    }

    static Ulid Ulid() {
        return UlidCreator.getUlid();
    }

    static UUID UUIDv1() {
        return UuidCreator.getTimeBased();
    }

    static UUID UUIDv2(final UuidLocalDomain domain, final int identifier) {
        return UuidCreator.getDceSecurity(domain, identifier);
    }

    static UUID UUIDv3(final UuidNamespace namespace, final String name) {
        return UuidCreator.getNameBasedMd5(namespace, name);
    }

    static UUID UUIDv4() {
        return UuidCreator.getRandomBased();
    }

    static UUID UUIDv5(final UuidNamespace namespace, final String name) {
        return UuidCreator.getNameBasedSha1(namespace, name);
    }

    static UUID UUIDv6() {
        return UuidCreator.getTimeOrdered();
    }

    static UUID UUIDv7() {
        return UuidCreator.getTimeOrderedEpoch();
    }
}
