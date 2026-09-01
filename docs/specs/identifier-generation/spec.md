---
type: spec
title: Identifier Generation
updated: 2026-09-01
---

## Purpose

Lets a caller obtain a fresh identifier value of a chosen kind — UUID (in any
of its seven standard versions), ULID, TSID or KSUID — through one static
entry point, `Identifiers.*`, instead of wiring up the underlying f4b6a3
creator classes directly.

## Requirements

### Requirement: A parameterless UUID can be generated in versions 1, 4, 6 or 7
`req~identifier-generation.uuid-parameterless~1`

`Identifiers` must offer one no-argument method per version for the four
UUID versions that need no caller-supplied input, each delegating to the
matching `UuidCreator` factory method: `UUIDv1()` to `getTimeBased()`,
`UUIDv4()` to `getRandomBased()`, `UUIDv6()` to `getTimeOrdered()` and
`UUIDv7()` to `getTimeOrderedEpoch()`.

#### Scenario: Time-based UUID (v1)

- **WHEN** `Identifiers.UUIDv1()` is called
- **THEN** the returned `UUID` is produced by `UuidCreator.getTimeBased()`

#### Scenario: Random UUID (v4)

- **WHEN** `Identifiers.UUIDv4()` is called
- **THEN** the returned `UUID` is produced by `UuidCreator.getRandomBased()`

#### Scenario: Time-ordered UUID (v6)

- **WHEN** `Identifiers.UUIDv6()` is called
- **THEN** the returned `UUID` is produced by `UuidCreator.getTimeOrdered()`

#### Scenario: Time-ordered epoch UUID (v7)

- **WHEN** `Identifiers.UUIDv7()` is called
- **THEN** the returned `UUID` is produced by
  `UuidCreator.getTimeOrderedEpoch()`

### Requirement: A UUID can be generated from caller-supplied namespace or domain data
`req~identifier-generation.uuid-parameterized~1`

`Identifiers` must offer `UUIDv2(domain, identifier)`, `UUIDv3(namespace,
name)` and `UUIDv5(namespace, name)`, each forwarding its arguments
unchanged to the matching `UuidCreator` factory method: `UUIDv2` to
`getDceSecurity(domain, identifier)`, `UUIDv3` to
`getNameBasedMd5(namespace, name)` and `UUIDv5` to
`getNameBasedSha1(namespace, name)`.

#### Scenario: DCE Security UUID (v2)

- **WHEN** `Identifiers.UUIDv2(domain, identifier)` is called with a
  `UuidLocalDomain` and an `int` local identifier
- **THEN** the returned `UUID` is produced by
  `UuidCreator.getDceSecurity(domain, identifier)`

#### Scenario: Name-based UUID, MD5 (v3)

- **WHEN** `Identifiers.UUIDv3(namespace, name)` is called with a
  `UuidNamespace` and a `String` name
- **THEN** the returned `UUID` is produced by
  `UuidCreator.getNameBasedMd5(namespace, name)`

#### Scenario: Name-based UUID, SHA-1 (v5)

- **WHEN** `Identifiers.UUIDv5(namespace, name)` is called with a
  `UuidNamespace` and a `String` name
- **THEN** the returned `UUID` is produced by
  `UuidCreator.getNameBasedSha1(namespace, name)`

### Requirement: A fresh ULID, TSID or KSUID can be generated
`req~identifier-generation.ulid-tsid-ksuid~1`

`Identifiers` must offer one no-argument method per remaining identifier
family, each delegating to the matching f4b6a3 creator: `Ulid()` to
`UlidCreator.getUlid()`, `Tsid()` to `TsidCreator.getTsid()` and `Ksuid()`
to `KsuidCreator.getKsuid()`.

#### Scenario: ULID

- **WHEN** `Identifiers.Ulid()` is called
- **THEN** the returned `Ulid` is produced by `UlidCreator.getUlid()`

#### Scenario: TSID

- **WHEN** `Identifiers.Tsid()` is called
- **THEN** the returned `Tsid` is produced by `TsidCreator.getTsid()`

#### Scenario: KSUID

- **WHEN** `Identifiers.Ksuid()` is called
- **THEN** the returned `Ksuid` is produced by `KsuidCreator.getKsuid()`
