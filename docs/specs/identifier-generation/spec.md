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
UUID versions that need no caller-supplied input: `UUIDv1()` (time-based),
`UUIDv4()` (random), `UUIDv6()` (time-ordered) and `UUIDv7()` (time-ordered
epoch). Each call returns a new `UUID` value.

#### Scenario: Time-based UUID (v1)

- **WHEN** `Identifiers.UUIDv1()` is called
- **THEN** a `UUID` value is returned

#### Scenario: Random UUID (v4)

- **WHEN** `Identifiers.UUIDv4()` is called
- **THEN** a `UUID` value is returned

#### Scenario: Time-ordered UUID (v6)

- **WHEN** `Identifiers.UUIDv6()` is called
- **THEN** a `UUID` value is returned

#### Scenario: Time-ordered epoch UUID (v7)

- **WHEN** `Identifiers.UUIDv7()` is called
- **THEN** a `UUID` value is returned

### Requirement: A UUID can be generated from caller-supplied namespace or domain data
`req~identifier-generation.uuid-parameterized~1`

`Identifiers` must offer `UUIDv2(domain, identifier)` (DCE Security),
`UUIDv3(namespace, name)` (name-based, MD5) and `UUIDv5(namespace, name)`
(name-based, SHA-1). Each forwards its arguments unchanged to the
corresponding f4b6a3 creator method and returns the resulting `UUID`.

#### Scenario: DCE Security UUID (v2)

- **WHEN** `Identifiers.UUIDv2(domain, identifier)` is called with a
  `UuidLocalDomain` and an `int` local identifier
- **THEN** a `UUID` value derived from that domain and identifier is returned

#### Scenario: Name-based UUID, MD5 (v3)

- **WHEN** `Identifiers.UUIDv3(namespace, name)` is called with a
  `UuidNamespace` and a `String` name
- **THEN** a `UUID` value derived from that namespace and name is returned

#### Scenario: Name-based UUID, SHA-1 (v5)

- **WHEN** `Identifiers.UUIDv5(namespace, name)` is called with a
  `UuidNamespace` and a `String` name
- **THEN** a `UUID` value derived from that namespace and name is returned

### Requirement: A fresh ULID, TSID or KSUID can be generated
`req~identifier-generation.ulid-tsid-ksuid~1`

`Identifiers` must offer one no-argument method per remaining identifier
family: `Ulid()`, `Tsid()` and `Ksuid()`.

#### Scenario: ULID

- **WHEN** `Identifiers.Ulid()` is called
- **THEN** a `Ulid` value is returned

#### Scenario: TSID

- **WHEN** `Identifiers.Tsid()` is called
- **THEN** a `Tsid` value is returned

#### Scenario: KSUID

- **WHEN** `Identifiers.Ksuid()` is called
- **THEN** a `Ksuid` value is returned
