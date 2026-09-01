---
type: spec
title: Identifier Text Codec
updated: 2026-09-01
---

## Purpose

Lets a caller turn an identifier value produced by `Identifiers.*` into a
`String` and back, choosing the text format through `Codecs.*` instead of
constructing a codec directly. UUID offers a choice between five text
formats; ULID, TSID and KSUID each offer exactly one.

## Requirements

### Requirement: A UUID can be encoded to and decoded from text in a chosen format
`req~identifier-text-codec.uuid-formats~1`

`Codecs` must offer one `Codec<UUID>` per supported text format —
`UUIDBase16()`, `UUIDBase32()`, `UUIDBase62()`, `UUIDBase64()` and
`UUIDBase64Url()`. Encoding and then decoding with the same codec must
return a `UUID` equal to the original.

#### Scenario: Round trip in one format

- **WHEN** a `UUID` is encoded with `Codecs.UUIDBase32()` and the resulting
  text is decoded with the same codec
- **THEN** the decoded `UUID` equals the original

#### Scenario: Formats are not interchangeable

- **WHEN** the same `UUID` is encoded once with `Codecs.UUIDBase16()` and
  once with `Codecs.UUIDBase32()`
- **THEN** the two resulting text values differ

### Requirement: A ULID, TSID or KSUID can be encoded to and decoded from its one text format
`req~identifier-text-codec.ulid-tsid-ksuid-format~1`

`Codecs` must offer `UlidBase32()` (Crockford Base32), `TsidBase32()`
(Crockford Base32) and `KsuidBase62()` (Base62). Encoding and then decoding
with the matching codec must return a value equal to the original.

#### Scenario: ULID round trip

- **WHEN** a `Ulid` is encoded with `Codecs.UlidBase32()` and the resulting
  text is decoded with the same codec
- **THEN** the decoded `Ulid` equals the original

#### Scenario: TSID round trip

- **WHEN** a `Tsid` is encoded with `Codecs.TsidBase32()` and the resulting
  text is decoded with the same codec
- **THEN** the decoded `Tsid` equals the original

#### Scenario: KSUID round trip

- **WHEN** a `Ksuid` is encoded with `Codecs.KsuidBase62()` and the
  resulting text is decoded with the same codec
- **THEN** the decoded `Ksuid` equals the original

### Requirement: A text value can be checked for validity without raising an exception
`req~identifier-text-codec.text-validation~1`

Each concrete codec (`UUIDCodec`, `UlidCodec`, `TsidCodec`, `KsuidCodec`)
must offer `isValid(String)`, returning whether the text decodes
successfully as that codec's identifier type, without throwing. `isValid`
is declared on the concrete codec enums, not on the `Codec<ID>` interface —
a caller holding only the `Codec<ID>` returned by `Codecs.*` must reference
the concrete enum (e.g. `UUIDCodec.Base32`) to reach it.

#### Scenario: Valid text

- **WHEN** `isValid(text)` is called with text that `decode` would parse
  successfully
- **THEN** it returns `true`

#### Scenario: Text that fails to decode

- **WHEN** `isValid(text)` is called with text that `decode` would reject
- **THEN** it returns `false`
- **AND** no exception is raised

#### Scenario: Null or blank text

- **WHEN** `isValid(text)` is called with `null` or a string that is empty
  after trimming
- **THEN** it returns `false`
- **AND** `decode` is not attempted
